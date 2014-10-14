package io.terminus.snz.user.manager;

import com.google.common.base.Objects;
import io.terminus.snz.user.dao.UserDao;
import io.terminus.snz.user.dto.RichSupplierDto;
import io.terminus.snz.user.model.User;
import io.terminus.snz.user.tool.HaierCasHttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkState;

/**
 * HAC 用户管理
 * CREATED BY: IntelliJ IDEA
 * AUTHOR: haolin
 * ON: 14-7-5
 */
public class AccountManagerHac extends AccountManager {

    @Autowired
    private UserDao userDao;

    @Override @Transactional
    public Long createSupplier(RichSupplierDto richSupplierDto) {
        // raw user for plain password
        User rawUser = new User();
        rawUser.setNick(richSupplierDto.getUser().getNick());
        rawUser.setEmail(richSupplierDto.getUser().getEmail());
        rawUser.setEncryptedPassword(richSupplierDto.getUser().getEncryptedPassword());
        // create into local
        Long id = super.createSupplier(richSupplierDto);
        // create supplier user into CAS
        createSupplierFromCas(rawUser);
        return id;
    }

    private void createSupplierFromCas(User user) {
        String result = HaierCasHttpUtil.register(user.getNick(), user.getEmail(), user.getEncryptedPassword());
        checkState(Objects.equal(result, "ok"), result);
    }

    /**
     * 更新用户信息:
     *  1. 更新本地用户信息
     *  2. 更新HAC用户信息
     * @param user 用户对象
     */
    public void updateUser(User user) {
        // 1. 更新本地用户信息
        userDao.update(user);
        // 2. 更新HAC
        // TODO 无接口
    }
}
