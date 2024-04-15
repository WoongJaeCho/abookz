
var readChange = document.getElementsByClassName("shelf_myShelf_read");
var popups = document.getElementsByClassName("popup");
var endDates = document.getElementsByClassName("shelf_myShelf_endDate");
var targetDates = document.getElementsByClassName("shelf_myShelf_target");
var updateButtons =  document.getElementsByClassName("shelf_myShelf_update");
var deleteButtons =  document.getElementsByClassName("shelf_myShelf_delete");
let modal = document.getElementById("myModal");
let modalContent = document.getElementById("modal_content");
function onRadioChange(popup, selectedValue) {
    let value = popup.getAttribute('data-value');
    console.log(value +"value");
    console.log(selectedValue);
    popup.style.display = 'none';

    let jsonData = {
            id : value,
            tag: selectedValue

    };
    fetch('/readingUpdate', {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            }
            if (!response.ok) {
                throw new Error('서버 응답이 실패했습니다.');
            }
            return response.json();
        })
        .then(data => {
            console.log('서버에서 받은 데이터:', data);
        })
        .catch(error => {
            console.error('오류 발생:', error);
        });
}
function deleteInput(deleteButton) {
    let value = deleteButton.getAttribute('data-value');
    console.log(value);
    let jsonData = {
        id : value,

    };
    fetch('/deleteMyShelf', {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => {
            if (response.redirected) {
                window.location.href = response.url;
            }
            if (!response.ok) {
                throw new Error('서버 응답이 실패했습니다.');
            }
            return response.json();
        })
        .then(data => {
            console.log('서버에서 받은 데이터:', data);
        })
        .catch(error => {
            console.error('오류 발생:', error);
        });
}

for (var i = 0; i < readChange.length; i++) {
    let element = readChange[i];
    let popup = popups[i];
    let targetDate = targetDates[i];
    let endDate = endDates[i];
    let deleteButton = deleteButtons[i];
    let  updateButton = updateButtons[i];
    element.addEventListener("click", function (event) {
        var x = event.clientX; // 클릭한 위치의 x 좌표
        var y = event.clientY; // 클릭한 위치의 y 좌표

        popup.style.position = 'fixed';
        popup.style.top = y + 'px';
        popup.style.left = x + 'px';
        popup.style.display = 'block';

        let radiosInPopup = popup.querySelectorAll('input[type="radio"][name="stats"]');
        radiosInPopup.forEach(function(radio) {
            radio.addEventListener('change', function() {
                if (this.checked) {
                    onRadioChange(popup, this.value);
                }
            });
        });
    });
    deleteButton.addEventListener("click",() =>{
        if(confirm("정말로 삭제하시겠습니까?")){
            console.log("진입");
            deleteInput(deleteButton);
        }else{

        }
    })
    updateButton.addEventListener("click", function() {
        openModal(modal,popup);
        console.log("모달 열기 버튼 클릭됨");
    });

}
