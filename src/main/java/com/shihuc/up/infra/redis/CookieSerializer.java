package com.shihuc.up.infra.redis;
//
//import org.springframework.session.web.http.DefaultCookieSerializer;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//
///**
// * @Author: chengsh05
// * @Date: 2018/12/19 12:29
// *
// * 遇到奇怪问题，在这里文件里面@Value取不到值，待解。。。
// *
// */
////@Component
////public class CookieSerializer extends DefaultCookieSerializer {
////
////    public CookieSerializer() {
////        super.setCookieName("JSESSIONID");
////        super.setUseBase64Encoding(false);
////    }
////
////    @Override
////    public void setCookieMaxAge(int cookieMaxAge) {
////        super.setCookieMaxAge(cookieMaxAge);
////    }
////
////    private String getCookiePath(HttpServletRequest request) {
////        return request.getContextPath();
////    }
////}
