
var readChange = document.getElementsByClassName("shelf_myShelf_read");
var popups = document.getElementsByClassName("popup");
var endDates = document.getElementsByClassName("shelf_myShelf_endDate");
var targetDates = document.getElementsByClassName("shelf_myShelf_target");
var updateButtons =  document.getElementsByClassName("shelf_myShelf_update");
var deleteButtons =  document.getElementsByClassName("shelf_myShelf_delete");
let modal = document.getElementById("myModal");
let modalContent = document.getElementById("modal_content");
let isInputCheck = false;
window.onclick = function(event) {
    if (event.target === modal) {
        closeModal(modal);
    }
}
function closeModal(modal) {
    modal.style.display = "none";
    modal.style.opacity="0";
}
// 모달창
function openModal(modal,popup) {
    let value = popup.getAttribute('data-value');
    modal.style.display = "block";
    modal.style.opacity="1";
    fetch("/modal?id=" +value,{
    method: "POST",
        headers: {
        'Content-Type':'application/json'
        },
        body: JSON.stringify({value : value})
    })
        .then(response =>response.json())
        .then(data =>{
            let startDate = data.bookShelfDTO.startDate ? moment(data.bookShelfDTO.startDate).format('YYYY-MM-DD') : '설정하기';
            let addDate = data.bookShelfDTO.addDate ? moment(data.bookShelfDTO.addDate).format('YYYY-MM-DD') : '설정하기';
            let targetDate = data.bookShelfDTO.targetDate ? moment(data.bookShelfDTO.targetDate).format('YYYY-MM-DD') : '설정하기';
            let endDate  = data.bookShelfDTO.endDate ? moment(data.bookShelfDTO.endDate).format('YYYY-MM-DD') : '설정하기';
            let table = document.createElement('table');
            table.className = 'modal-table'; // 클래스 추가
            table.innerHTML=
                '<tr><td colspan="5" class="modal_title"> 첵 제목:' + data.bookDTO.title + '</td></tr>' +
                '<tr><td rowspan="5" class="modal_img"><img src="' + data.bookDTO.cover + '" alt="이미지"></td></tr>'
                + '<tr><td colspan="4" class="modal_addDate"> 추가한 날짜 : ' + addDate+ '</td></tr>'
                + '<tr><td colspan="4" class="modal_startDate"> 시작한 날짜 : ' + startDate + '</td></tr>'
                +'<tr><td colspan="4" class="modal_startDate"> 목표 날짜 : ' + targetDate + '</td></tr>'
                +'<tr><td colspan="4" id="modal_endDate"> 완독 날짜 : ' + endDate + '</td></tr>'
            +'<tr><td colspan="5" class="modal_page"><div class="progress-bar"><div class="progress"></div></div><div class="startPage">현재 : '+data.bookShelfDTO.currentPage +'</div><div class="endPage">마지막페이지 : ' +data.bookDTO.itemPage+'</div></td></tr>';
            modalContent.appendChild(table);
            let endClick= document.getElementById('modal_endDate');
            endClick.addEventListener("click", function (event){
                var x = event.clientX; // 클릭한 위치의 x 좌표
                var y = event.clientY; // 클릭한 위치의 y 좌표
                if(isInputCheck){
                    return;
                }
                var input = document.createElement("input");
                input.type = "text";
                input.className = "endDate"; // 클래스 설정
                console.log(input.value);
                // 생성한 input 요소를 컨테이너에 추가

                endClick.appendChild(input);
                // flatpickr 적용
                flatpickr(input, {});
                isInputCheck = true;
                flatpickr(input, {
                    onChange: function(selectedDates, dateStr, instance) {
                        console.log(dateStr);
                        endClick.removeChild(input);
                        isInputCheck = false;
                    }
                });
            });


        })
}



function onRadioChange(popup, selectedValue) {
    let value = popup.getAttribute('data-value');
    console.log(value);
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
  /*  endDate.addEventListener("click", function (event){
        var x = event.clientX; // 클릭한 위치의 x 좌표
        var y = event.clientY; // 클릭한 위치의 y 좌표
        if(isInputCheck){
            return;
        }
        var input = document.createElement("input");
        input.type = "text";
        input.className = "endDate"; // 클래스 설정
        console.log(input.value);
        // 생성한 input 요소를 컨테이너에 추가
        endDate.appendChild(input);
        // flatpickr 적용
        flatpickr(input, {});
        isInputCheck = true;
        flatpickr(input, {
            onChange: function(selectedDates, dateStr, instance) {
                console.log(dateStr);
                endDate.removeChild(input);
                isInputCheck = false;
            }
        });
    });*/

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