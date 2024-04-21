function changeRole(){
    let selectedRole = document.getElementById("changeRole");
    let changedRole = selectedRole.options[selectedRole.selectedIndex].value.trim();
    console.log(changedRole);
    let id = document.getElementById("number").innerText;
    let loginId = document.getElementById("loginId").innerText;
    let password= document.getElementById("password").innerText;
    let email = document.getElementById("email").innerText;
    let name = document.getElementById("name").innerText;
    let profile = document.getElementById("profile").innerText;
    let regDate = document.getElementById("regDate").innerText;

    let list = {
        id: id,
        loginId: loginId,
        password: password,
        email: email,
        name: name,
        role: changedRole,
        profile: profile,
        regDate: regDate
    }
    console.log(list);

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

