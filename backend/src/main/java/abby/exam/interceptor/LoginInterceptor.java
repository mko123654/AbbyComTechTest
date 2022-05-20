/***********************************************************
 * @Description : 登入攔截器類別，主要用在驗證Token
 * @author      : Abby Chang (2004012)
 * @date        : 2022-05-18 18:57
 * @email       : mko123654@gmail.com
 ***********************************************************/
package abby.exam.interceptor;

import abby.exam.utils.JwtUtils;
import abby.exam.vo.JsonData;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.logging.Logger;

/**
 * 參考內容：
 * https://stackoverflow.com/questions/43591582/application-properties-value-in-spring-boot-interceptor
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    Logger logger = Logger.getLogger(LoginInterceptor.class.getName());

    /**
     * 有上面的@Component由application.yml中取得設定的值
     */
    @Value("${interceptors.auth-ignore-uris}")
    private String authIgnoreUris;

    /**
     * 進入controller之前進行攔截
     *
     * @param request
     * @param response
     * @param handler
     * @return boolean 是否被攔截
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("進入攔截器");
        String uri = request.getRequestURI();
        logger.info("uri：" + uri);
        logger.info("不須驗證的API：" + authIgnoreUris);
        String[] authIgnoreUriArr = authIgnoreUris.split(",");
        // 登入和註冊API不需要進行token的攔截和驗證
        for (String authIgnoreUri : authIgnoreUriArr) {
            if (authIgnoreUri.equals(uri)) {
                return true;
            }
        }
        // 注意要和前端搭配Access-Token屬性，前端會在登入後的每個API的request header加上Access-Token屬性
        String token = request.getHeader("Access-Token");
        if (token == null) {
            // token不在header中時，檢查是否在參數內(RequestParam)
            token = request.getParameter("token");
        }
        if (token != null) {
            // request中帶有參數
            Claims claims = JwtUtils.checkJWT(token);
            if (claims == null) {
                // 若為null表示token被竄改，或已經過期，驗證失敗
                sendJsonMessage(response, JsonData.buildError("token無效，請重新登入"));
                return false;
            }

            String id = (String) claims.get("id");
            String username = (String) claims.get("username");
            // 放進請求中，以便在controller中可以使用，不需要在controller中在用Jwt解密了
            request.setAttribute("user_id", id);
            request.setAttribute("username", username);
            return true;
        }
        sendJsonMessage(response, JsonData.buildError("token为null，請重新登入"));
        return false;
    }

    /**
     * 回傳錯誤訊息給前端
     *
     * @param response
     * @param obj
     *
     * @throws Exception
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) throws Exception {
        Gson g = new Gson();
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(g.toJson(obj));
        writer.close();
        response.flushBuffer();
    }
}
