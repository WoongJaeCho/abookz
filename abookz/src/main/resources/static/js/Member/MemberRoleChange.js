var selectedRole = document.getElementsByClassName('changeRole');
var getId = document.getElementsByClassName('number');

function changeRole() {
    console.log(selectedRole)
    for (var i = 0; i < selectedRole.length; i++) {
        selectedRole[i].addEventListener('change', createEventListener(i));
    }
}

function createEventListener(index) {
    return function () {
        let selectedValue = this.value;
        let id = getId[index].getAttribute('id-value');
        console.log(id, selectedValue);
        getOneChange(id, selectedValue);
    };
}

changeRole();

function getOneChange(id, role){
    const data= {
        id : id,
        role : role
    }
    fetch("/member/changeRole", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(data)
    })
        .then(response => response.text())
        .then(data=> {
            if(data === "confirm"){
                alert("성공!")
                window.location.reload(true);
            }
            else{
                alert("오류발생!")
                location.assign("/member/list");
            }
        })
}
