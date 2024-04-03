function Logout(){
    fetch("/member/logout", {
        method: "GET"
    })
        .then(response => response.text())
        .then(data => {
            if(data === "confirm"){
                alert("로그아웃 완료");
                location.href = "/";
            }
        })
        .catch(error => alert("error"));
}