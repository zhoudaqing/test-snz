///*
// * Copyright (c) 2012 杭州端点网络科技有限公司
// */
//
//package io.terminus.pampas.console.controller.api.design;
//
//import com.google.common.base.Function;
//import com.google.common.base.Strings;
//import com.google.common.base.Throwables;
//import com.google.common.collect.ImmutableSet;
//import com.google.common.collect.Lists;
//import com.google.common.io.Files;
//import io.terminus.pampas.console.extend.ImageServer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
//@Controller
//@RequestMapping("/api/design/images")
//public class Images {
//    private final static Logger log = LoggerFactory.getLogger(Images.class);
//    private final static Set<String> allowed_types = ImmutableSet.of("jpeg", "jpg", "png", "gif");
//
//    @Autowired
//    private ImageService imageService;
//
//    @Autowired
//    private ImageServer imageServer;
//
//    @Value("#{app.imageBaseUrl}")
//    private String imageBaseUrl;
//
//    @Value("#{app.imgSizeMax}")
//    private long imgSizeMax;
//
//
//    @RequestMapping(value = "/{imageId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public void deleteImage(@PathVariable("imageId") Long imageId) {
//        BaseUser user = UserUtil.getCurrentUser();
//        if (user == null) {
//            throw new JsonResponseException(401, messageSources.get("user.not.login"));
//        }
//        Response<UserImage> userImageR = imageService.findUserImageById(imageId);
//        if (!userImageR.isSuccess()) {
//            log.warn("failed to find userImage by imageId {} when delete", imageId);
//            return;
//        }
//        UserImage userImage = userImageR.getResult();
//        if (!userImage.getUserId().equals(user.getId())) {
//            throw new JsonResponseException(401, messageSources.get("image.delete.noauth"));
//        }
//        imageService.deleteUserImage(userImage);
//        try {
//            imageServer.delete(userImage.getFileName());
//        } catch (Exception e) {
//            log.warn("error happened when deleteFile {} on upyun, error:{}", userImage, e);
//        }
//    }
//
//    @RequestMapping(value = "/batch_delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public int batchDelete(@RequestParam("imageIds[]") Long[] imageIds) {
//        BaseUser user = UserUtil.getCurrentUser();
//        if (user == null) {
//            throw new JsonResponseException(401, messageSources.get("user.not.login"));
//        }
//        int successCount = 0;
//        for (Long imageId : imageIds) {
//            Response<UserImage> userImageR = imageService.findUserImageById(imageId);
//            if (!userImageR.isSuccess()) {
//                log.warn("failed to find userImage by imageId {} when delete", imageId);
//                continue;
//            }
//            UserImage userImage = userImageR.getResult();
//            if (!userImage.getUserId().equals(user.getId())) {
//                log.warn("image {} not belong to user {}", userImage.getId(), user.getId());
//                continue;
//            }
//            imageService.deleteUserImage(userImage);
//            try {
//                imageServer.delete(userImage.getFileName());
//            } catch (Exception e) {
//                log.warn("error happened when deleteFile {} on upyun, error:{}", userImage, e);
//                continue;
//            }
//            successCount++;
//        }
//        return successCount;
//    }
//
//    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
//    @ResponseBody
//    public String processUpload(@RequestParam(required = false) String category, MultipartHttpServletRequest request) {
//        BaseUser user = UserUtil.getCurrentUser();
//        if (user == null) {
//            throw new JsonResponseException(401, messageSources.get("user.not.login"));
//        }
//        Iterator<String> fileNameItr = request.getFileNames();
//        List<UploadedFile> result = Lists.newArrayList();
//        while (fileNameItr.hasNext()) {
//            String name = fileNameItr.next();
//            MultipartFile file = request.getFile(name);
//            Long userId = user.getId();
//            String originalFilename = userId + "_" +file.getOriginalFilename();
//            String ext = Files.getFileExtension(originalFilename).toLowerCase();
//            if (allowed_types.contains(ext)) {
//                try {
//                    byte[] imageData = file.getBytes();
//
//                    //if size of the image is more than imgSizeMax,it will raise an 500 error
//                    if (imageData.length > imgSizeMax) {
//                        log.debug("image size {} ,maxsize {} ,the upload image is to large", imageData.length, imgSizeMax);
//                        result.add(new UploadedFile(name, messageSources.get("image.size.exceed", imgSizeMax / (1024 * 1024), "mb")));
//                        continue;
//                    }
//
//                    String fileName = imageServer.handleFileName(originalFilename, imageData);
//                    String filePath = imageServer.write(fileName, file);
//                    //若成功返回路径则代表上传成功
//                    boolean isSucceed = !Strings.isNullOrEmpty(filePath);
//                    if (!isSucceed) {
//                        log.error("write file(name={}) of user(id={}) to image server failed", fileName, userId);
//                        result.add(new UploadedFile(name, messageSources.get("image.upload.fail")));
//                        continue;
//                    }
//                    UserImage userImage = new UserImage();
//                    userImage.setUserId(userId);
//                    userImage.setCategory(Strings.isNullOrEmpty(category) ? null : category);
//                    userImage.setFileName(filePath);
//                    userImage.setFileSize((int) file.getSize());
//                    imageService.addUserImage(userImage);
//                    UploadedFile u = new UploadedFile(userImage.getId(), originalFilename,
//                            Long.valueOf(file.getSize()).intValue(),
//                            imageBaseUrl + filePath);
//                    result.add(u);
//
//                } catch (Exception e) {
//                    log.error("failed to process upload image {},cause:{}", originalFilename, Throwables.getStackTraceAsString(e));
//                    result.add(new UploadedFile(name, messageSources.get("image.upload.fail")));
//                }
//            } else {
//                result.add(new UploadedFile(name, messageSources.get("image.illegal.ext")));
//            }
//        }
//
//        int errorCount = 0;
//        for (UploadedFile u : result) {
//            if (u.getErrorInfo() != null) {
//                errorCount++;
//            }
//        }
//        // 如果全失败了就抛错 保持之前上传单个图片时的行为
//        if (errorCount == result.size()) {
//            throw new JsonResponseException(500, result.get(0).getErrorInfo());
//        }
//        return JsonMapper.JSON_NON_EMPTY_MAPPER.toJson(result);
//    }
//
//    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public Paging<UploadedFile> imagesOf(@RequestParam(required = false) String category,
//                                         @RequestParam(value = "p", defaultValue = "1") Integer pageNo,
//                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
//        BaseUser user = UserUtil.getCurrentUser();
//        if (user == null) {
//            throw new JsonResponseException(401, messageSources.get("user.not.login"));
//        }
//        category = Strings.isNullOrEmpty(category) ? null : category;
//        Integer from = (pageNo - 1) * size;
//        Response<Paging<UserImage>> result = imageService.findUserImages(user.getId(), category, from, size);
//        if (!result.isSuccess()) {
//            log.error("failed to find user images for pageNo={} and size={},cause:{}",
//                    pageNo, size, result.getError());
//            throw new JsonResponseException(500, messageSources.get("image.query.fail"));
//        }
//        Paging<UserImage> userImageP = result.getResult();
//        return new Paging<UploadedFile>(userImageP.getTotal(), Lists.transform(userImageP.getData(), new Function<UserImage, UploadedFile>() {
//            @Override
//            public UploadedFile apply(UserImage input) {
//                return new UploadedFile(input.getId(), null, input.getFileSize(), imageBaseUrl + input.getFileName());
//            }
//        }));
//    }
//
//}
