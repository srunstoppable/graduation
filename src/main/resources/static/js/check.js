$(document).ready(function () {
   if( window.localStorage.getItem("token")==null){
       window.location.href = "/login_view.html";
   }
});