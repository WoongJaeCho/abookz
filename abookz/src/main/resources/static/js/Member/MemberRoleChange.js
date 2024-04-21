function changeRole(){
    let selectedRole = document.getElementById("changeRole");
    let changedRole = selectedRole.options[selectedRole.selectedIndex].value;
    console.log(changedRole);
    let id = document.getElementById("number");
    let loginId = document.getElementById("loginId");
    let password= document.getElementById("password");
    let email = document.getElementById("email");
    let name = document.getElementById("name");
    let profile = document.getElementById("profile");
    let regDate = document.getElementById("regDate");

    let list = {
        id: id.value(),
        loginId: loginId.value(),
        password: password.value(),
        email: email.value(),
        name: name.value(),
        role: changedRole,
        profile: profile.value(),
        regDate: regDate.value()
    }

    fetch("/member/changeRole", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(list)
    })
        .then(response => response.text())
        .then(data=> {
            if(data === "confirm"){
                alert("성공!")
                location.href = "member/list"
            }
            else{
                alert("오류 발생!")
                location.href = "member/list"
            }
        })
}

