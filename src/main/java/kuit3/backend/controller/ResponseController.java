package kuit3.backend.controller;

import kuit3.backend.common.response.BaseErrorResponse;
import kuit3.backend.common.response.BaseResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kuit3.backend.common.response.status.BaseExceptionResponseStatus.BAD_REQUEST;

@RestController
public class ResponseController {

    @RequestMapping("/base-response")
    public BaseResponse<UserData> getSuccessResponse(){
        UserData userdata = new UserData("park",20);
        return new BaseResponse<>(userdata);
    }
    @RequestMapping("/base-error-response")
    public BaseErrorResponse getFailedResponse(){
        return new BaseErrorResponse(BAD_REQUEST);
    }
}
