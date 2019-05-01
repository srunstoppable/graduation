// 修改 后台获取的时间的格式在前台展示
function createTime(v) {
    var date = new Date(v);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? '0' + m : m;
    var d = date.getDate();
    d = d < 10 ? ("0" + d) : d;
    var h = date.getHours();
    h = h < 10 ? ("0" + h) : h;
    var M = date.getMinutes();
    M = M < 10 ? ("0" + M) : M;
    var s=date.getSeconds();
    s=s<10?("0"+s):s;
    var str = y + "-" + m + "-" + d + " " + h + ":" + M+":"+s;
    return str;
}




