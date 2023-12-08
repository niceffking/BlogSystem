// 根据当前key 获取url中的value
function getParamValue(key) {
    // 获取参数部分
    var params = location.search;
    if (params.indexOf("?") >= 0) {
        // 根据 & 将其分割成多个数组
        params = params.substring(1);
        
        var paramsArr = params.split('&');
        if (paramsArr.length >= 1) {
            // 说明有key
            for(var i = 0; i <paramsArr.length; i++) {
                // key : value
                var key_value = paramsArr[i].split("=");
                if (key == key_value[0]) {
                    return key_value[1];
                }
            }
        }
    }
    return null;

    // 去除?
    

    // 训话查找key 和 value


}