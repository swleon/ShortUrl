# 短链接服务接口文档


**简介**:短链接服务接口文档


**HOST**:http://localhost:8082


**联系人**:wuque


**Version**:1.0


**接口路径**:/v3/api-docs


[TOC]






# url-controller


## visit


**接口地址**:`/api/vlink/go/{shortUrl}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|shortUrl|shortUrl|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|ResponseEntity|
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|body||object||
|statusCode|可用值:ACCEPTED,ALREADY_REPORTED,BAD_GATEWAY,BAD_REQUEST,BANDWIDTH_LIMIT_EXCEEDED,CHECKPOINT,CONFLICT,CONTINUE,CREATED,DESTINATION_LOCKED,EXPECTATION_FAILED,FAILED_DEPENDENCY,FORBIDDEN,FOUND,GATEWAY_TIMEOUT,GONE,HTTP_VERSION_NOT_SUPPORTED,IM_USED,INSUFFICIENT_SPACE_ON_RESOURCE,INSUFFICIENT_STORAGE,INTERNAL_SERVER_ERROR,I_AM_A_TEAPOT,LENGTH_REQUIRED,LOCKED,LOOP_DETECTED,METHOD_FAILURE,METHOD_NOT_ALLOWED,MOVED_PERMANENTLY,MOVED_TEMPORARILY,MULTIPLE_CHOICES,MULTI_STATUS,NETWORK_AUTHENTICATION_REQUIRED,NON_AUTHORITATIVE_INFORMATION,NOT_ACCEPTABLE,NOT_EXTENDED,NOT_FOUND,NOT_IMPLEMENTED,NOT_MODIFIED,NO_CONTENT,OK,PARTIAL_CONTENT,PAYLOAD_TOO_LARGE,PAYMENT_REQUIRED,PERMANENT_REDIRECT,PRECONDITION_FAILED,PRECONDITION_REQUIRED,PROCESSING,PROXY_AUTHENTICATION_REQUIRED,REQUESTED_RANGE_NOT_SATISFIABLE,REQUEST_ENTITY_TOO_LARGE,REQUEST_HEADER_FIELDS_TOO_LARGE,REQUEST_TIMEOUT,REQUEST_URI_TOO_LONG,RESET_CONTENT,SEE_OTHER,SERVICE_UNAVAILABLE,SWITCHING_PROTOCOLS,TEMPORARY_REDIRECT,TOO_EARLY,TOO_MANY_REQUESTS,UNAUTHORIZED,UNAVAILABLE_FOR_LEGAL_REASONS,UNPROCESSABLE_ENTITY,UNSUPPORTED_MEDIA_TYPE,UPGRADE_REQUIRED,URI_TOO_LONG,USE_PROXY,VARIANT_ALSO_NEGOTIATES|string||
|statusCodeValue||integer(int32)|integer(int32)|


**响应示例**:
```javascript
{
	"body": {},
	"statusCode": "",
	"statusCodeValue": 0
}
```


## to


**接口地址**:`/api/vlink/to`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|url|url|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## to


**接口地址**:`/api/vlink/toWithEnableTransParms`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|url|url|query|true|string||
|enableTransParms|enableTransParms|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||
|401|Unauthorized||
|403|Forbidden||
|404|Not Found||


**响应参数**:


暂无


**响应示例**:
```javascript

```