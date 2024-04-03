function checkLogin(form){
    let id = form.loginId.value.trim();
    let pw = form.password.value.trim();

    if(id.length === 0 || pw.length === 0){
        alert("값을 다 넣어주세요");
        return;
    }

    fetch("/member/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
        },
        body: "id=" + id + "&pw=" + pw
    })
        .then(response => response.text())
        .then(data => {
            if(data === "null"){
                alert("로그인 실패");
                location.href = "/member/login";
            }
            else{
                alert("로그인 성공")
                location.href = "/";
            }
        })
        .catch(error => alert("error"))
        form.id.value = "";
        form.password.value = "";
}