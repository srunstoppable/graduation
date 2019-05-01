package com.example.graduation.controller;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author s r
 * @date 2018/11/21
 */
public class BaseController {
    protected final org.slf4j.Logger logger =
            LoggerFactory.getLogger(this.getClass().getSimpleName());

//    public static final String MSG = "msg";//成功消息
//    public static final String VERROR = "verror";//验证错误消息
//    public static final String RELOAD = "reload";//是否重新加载图片验证码

    public static final String PAGINATION_PARAM_PAGE = "page";
    public static final String PAGINATION_PARAM_SIZE = "size";

//    public static final String PAGINATION_PARAM_TOTAL = "total";
//    public static final String PAGINATION_PARAM_PAGES = "pages";

//    public static final String SEARCH_LIST_RESULT = "records";
//
//    public static final String NO_RIGHT_ERROR = "该功能暂未开放";
//    public static final String NO_DATA_ERROR = "没有找到匹配记录";
//    public static final String CREATE_SUCCESS_MSG = "创建成功";
//    public static final String OPT_FAIL_ERROR = "操作失败，请联系客服";
//    public static final String UPLOAD_FAIL_ERROR = "上传失败，请联系客服";

    public static boolean DEVELOP_ENV = false;

    /**
     * 手机号正则
     */
//    public final static String REPX_MOBILE = "^(13[0-9]|14[0-9]|15[0-9]|17[0-9]|18[0-9])\\d{8}$";

    @Autowired(required = false)
    protected HttpServletRequest request;
    @Autowired(required = false)
    protected HttpServletResponse response;

    public boolean isEmptyStr(String str) {
        return str == null || str.trim().isEmpty();
    }


    protected Integer[] getPageSizeFromGetRequest() {
        String pageStr = request.getParameter(PAGINATION_PARAM_PAGE);
        String sizeStr = request.getParameter(PAGINATION_PARAM_SIZE);


        /**
         * 分页传值
         */
        int page = 1;
        int size = 10;
        if (!isEmptyStr(pageStr)) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (Exception e) {
            }
        }
        if (!isEmptyStr(sizeStr)) {
            try {
                size = Integer.parseInt(sizeStr);
            } catch (Exception e) {
            }
        }
        return new Integer[]{page, size};
    }

//    /**
//     * 判断手机号合法性
//     *
//     * @param mobile
//     * @return
//     */
//    protected static boolean checkMobile(String mobile) {
//        if (StringUtils.isEmpty(mobile)) {
//            return false;
//        }
//        Pattern p = Pattern.compile(REPX_MOBILE);
//        Matcher m = p.matcher(mobile);
//        return m.matches();
//    }

    protected boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
