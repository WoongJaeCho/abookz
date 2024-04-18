let check = 0;
function validcheck(form){
    if(!form.loginId.value.trim()){
        alert("아이디를 입력해주세요");
        form.loginId.focus();
        return false;
    }
    if(!form.password.value.trim()){
        alert("비밀번호를 입력해주세요");
        form.password.focus();
        return false;
    }
    // if(!form.password.value.match(/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/)){
    //     alert("영문, 숫자, 특수기호 조합해서 8자리 이상 15자리 이하로 입력해주세요");
    //     form.password.focus();
    //     return false;
    // }
    if(!form.email.value.match(/[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$/)){
        alert("이메일 형식이 다릅니다");
        form.email.focus();
        return false;
    }
    if(!form.name.value.trim()){
        alert("닉네임을 입력해주세요");
        form.name.focus();
        return false;
    }

    if(check === 0){
        alert("Id 중복체크 해주세요");
        return false;
    }
    else if(check === -1){
        alert("Id 중복체크 다시하세요");
        return false
    }

    form.submit();
}

document.getElementById("checkId").addEventListener("click", () => {
    let id = document.getElementById("logId").value.trim();

    if(id.length === 0){
        alert("Id 값을 입력해주세요");
        document.getElementById("logId").focus();
        document.getElementById("logId").style.border = "";
        return;
    }

    fetch("/member/validId", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
        },
        body:"id=" + id,
    })
        .then(response => response.text())
        .then(getResult)
        .catch(() => alert("error"))
})

function getResult(data){
    if(data === "valid"){
        alert("이 아이디는 사용가능 합니다");
        document.getElementById("pw").focus();
        document.getElementById("logId").style.border = "3px blue solid";
        check = 1;
    }
    else if(data === "notValid"){
        alert("이 아이디는 사용 불가능 합니다");
        document.getElementById("logId").value = "";
        document.getElementById("logId").focus();
        document.getElementById("logId").style.border = "3px red solid";
        check = -1;
    }
}

document.getElementById("logId").addEventListener("keyup", (e) => {
    if(e.key === "Backspace" || e.keyCode === 8){
        check = 0;
    }
    document.getElementById("logId").style.border = "";
});