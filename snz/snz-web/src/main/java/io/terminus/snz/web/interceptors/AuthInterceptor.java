package io.terminus.snz.web.interceptors;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import com.google.common.net.HttpHeaders;
import io.terminus.common.exception.JsonResponseException;
import io.terminus.pampas.common.BaseUser;
import io.terminus.pampas.common.UserUtil;
import io.terminus.pampas.engine.exception.UnAuthorize401Exception;
import io.terminus.snz.user.model.User;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkState;

/**
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-18
 */
@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter{

    private final String mainSite;

    private final Set<WhiteItem> whiteList;

    private final Set<AuthItem> protectList;


    public AuthInterceptor(String mainSite) throws Exception {
        this.mainSite = mainSite;

        this.protectList = Sets.newHashSet();
        Yaml yaml = new Yaml();
        Auths auths = yaml.loadAs(Resources.toString(Resources.getResource("auth/auths.yaml"), Charsets.UTF_8), Auths.class);
        for (Auth auth : auths.auths) {
            Pattern urlPattern = Pattern.compile("^" + auth.url + "$");
            Set<Integer> types = Sets.newHashSet();
            if (auth.types != null && !auth.types.isEmpty()) {
                for (String type : auth.types) {
                    if (Objects.equal("ALL", type.toUpperCase())) { //if all, means everyone(if login in)  can access this url
                        for (User.Type t : User.Type.values()) {
                            types.add(t.value());
                        }
                    } else {
                        types.add(User.Type.fromName(type).value());
                    }
                }
            }

            Set<String> roles = Sets.newHashSet();
            if(auth.roles!=null && !auth.roles.isEmpty()){
                roles.addAll(auth.roles);
            }

            AuthItem authItem = new AuthItem(urlPattern,types, roles);
            protectList.add(authItem);
        }

        whiteList = Sets.newHashSet();
        Resources.readLines(Resources.getResource("auth/white_list"), Charsets.UTF_8, new LineProcessor<Void>() {
            @Override
            public boolean processLine(String line) throws IOException {
                if (!Strings.isNullOrEmpty(line)) {
                    List<String> parts = Splitter.on(':').trimResults().splitToList(line);
                    checkState(parts.size() == 2, "illegal white_list configuration [%s]", line);
                    Pattern urlPattern = Pattern.compile("^" + parts.get(0) + "$");
                    String methods = parts.get(1).toLowerCase();
                    ImmutableSet.Builder<String> httpMethods = ImmutableSet.builder();
                    for (String method : Splitter.on(',').omitEmptyStrings().trimResults().split(methods)) {
                        httpMethods.add(method);
                    }
                    whiteList.add(new WhiteItem(urlPattern, httpMethods.build()));

                }
                return true;
            }

            @Override
            public Void getResult() {
                return null;
            }
        });
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI().substring(request.getContextPath().length());
        BaseUser user = UserUtil.getCurrentUser();

        String method = request.getMethod().toLowerCase();
        for (WhiteItem whiteItem : whiteList) {  //method and uri matches with white list, ok
            if (whiteItem.httpMethods.contains(method) && whiteItem.pattern.matcher(requestURI).matches()) {
                return true;
            }
        }

        boolean inProtectedList = false;
        for (AuthItem authItem : protectList) { //protected url should be authorized
            if (authItem.pattern.matcher(requestURI).matches()) {
                inProtectedList = true;
                if (user != null) {    //用户已登陆
                    if (typeMatch(authItem.types, user.getType())) { //用户类型匹配
                        return true;
                    }
                    if(roleMatch(authItem.roles, user.getRoles())){  //用户角色匹配
                        return true;
                    }
                } else { //用户未登陆
                    redirectToLogin(request, response);
                    return false;
                }
            }
        }

        if (inProtectedList) {
            //能进入这里的,说明接口受到保护,用户已登陆,但用户角色不匹配,因此抛出鉴权失败的异常
            throw new UnAuthorize401Exception("您无权进行此操作");
        }

        if (!Objects.equal(method, "get") && user == null) { //write operation need login
            redirectToLogin(request, response);
            return false;
        }
        return true;
    }

    private boolean typeMatch(Set<Integer> expectedType, Integer actualType) {
        return expectedType.contains(actualType);
    }

    private boolean roleMatch(Set<String> expectedRoles, List<String> actualRoles) {
        for (String actualRole : actualRoles) {
            if(expectedRoles.contains(actualRole)){
                return true;
            }
        }
        return false;
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isAjaxRequest(request)) {
            throw new JsonResponseException(HttpStatus.UNAUTHORIZED.value(), "用户未登录");
        }
        String currentUrl = request.getRequestURL().toString();

        if (!Strings.isNullOrEmpty(request.getQueryString())) {
            currentUrl = currentUrl + "?" + request.getQueryString();
        }

        UriComponents uriComponents =
                UriComponentsBuilder.fromUriString("http://" + mainSite + "/haier-login?target={target}").build();
        URI uri = uriComponents.expand(currentUrl).encode().toUri();
        response.sendRedirect(uri.toString());
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return Objects.equal(request.getHeader(HttpHeaders.X_REQUESTED_WITH), "XMLHttpRequest");
    }

    @ToString
    private static class Auth {
        public String url;

        public List<String> types;

        public List<String> roles;
    }

    @ToString
    private static class Auths {
        public List<Auth> auths;
    }

    public static class AuthItem {
        public final Pattern pattern;

        public final Set<Integer> types;

        public final Set<String> roles;

        public AuthItem(Pattern pattern, Set<Integer> types, Set<String> roles) {
            this.pattern = pattern;
            this.types = types;
            this.roles = roles;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AuthItem authItem = (AuthItem) o;

            if (pattern != null ? !pattern.equals(authItem.pattern) : authItem.pattern != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return pattern != null ? pattern.hashCode() : 0;
        }
    }

    public static class WhiteItem {
        public final Pattern pattern;

        public final Set<String> httpMethods;

        public WhiteItem(Pattern pattern, Set<String> httpMethods) {
            this.pattern = pattern;
            this.httpMethods = httpMethods;
        }
    }
}
