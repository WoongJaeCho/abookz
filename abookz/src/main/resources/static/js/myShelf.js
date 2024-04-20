
var readChange = document.getElementsByClassName("shelf_myShelf_read");
var popups = document.getElementsByClassName("popup");
var endDates = document.getElementsByClassName("shelf_myShelf_endDate");
var targetDates = document.getElementsByClassName("shelf_myShelf_target");
var updateButtons =  document.getElementsByClassName("shelf_myShelf_update");
var deleteButtons =  document.getElementsByClassName("shelf_myShelf_delete");
let modal = document.getElementById("myModal");
let modalContent = document.getElementById("modal_content");
let isPopupCheck=  false;
function onRadioChange(popup, selectedValue) {
    let value = popup.getAttribute('data-value');

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
    isPopupCheck = false;
}

for (var i = 0; i < readChange.length; i++) {
    let element = readChange[i];
    let popup = popups[i];
    let targetDate = targetDates[i];
    let endDate = endDates[i];
    let deleteButton = deleteButtons[i];
    let  updateButton = updateButtons[i];
    element.addEventListener("click", function (event) {
        if(isPopupCheck){
            return;
        }

        var x = event.pageX;
        var y = event.pageY;
        popup.style.position = 'absolute';
        popup.style.top = y + 'px';
        popup.style.left = x + 'px';
        popup.style.display = 'block';

        isPopupCheck = true;
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

            deleteInput(deleteButton);
        }else{

        }
    })
    updateButton.addEventListener("click", function() {
        openModal(modal,popup);

    });

}
