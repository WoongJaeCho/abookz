
let isInputCheck = false;

// 모달창
function openModal(modal, popup) {
    let value = popup.getAttribute('data-value');
    console.log("check");
    modal.style.display = "block";
    modal.style.opacity = "1";
 disableClickOutside();
    function closeModal(modal) {
        console.log("갑체크")
        modal.style.display = "none";
        modal.style.opacity = "0";
        enableClickOutside();
    }x

    fetch("/modal?id=" + value, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ value: value })
    })
        .then(response => response.json())
        .then(data => {
            function  checkModal(){
                console.log("asdsadasdasd");
            }
                let table = document.createElement('table');
            table.className = 'modal-table';
            console.log("check1234");
            // 첵 제목 행
            let titleRow = document.createElement('tr');
            let titleCell = document.createElement('td');
            titleCell.colSpan = "5";
            titleCell.className = "modal_title";
            titleCell.textContent = "첵 제목: " + data.bookDTO.title;
            titleRow.appendChild(titleCell);
            const closeBtnCell = document.createElement('td');
            closeBtnCell.innerHTML = '<div id="myShelfModalClose" class="close-modal-button">X</div>';
           closeBtnCell.addEventListener("click", closeModal);
            titleRow.appendChild(closeBtnCell);
            table.appendChild(titleRow);

            // 이미지 행
            let imgRow = document.createElement('tr');
            let imgCell = document.createElement('td');
            imgCell.rowSpan = "5";
            imgCell.className = "modal_img";
            let img = document.createElement('img');
            img.src = data.bookDTO.cover;
            img.alt = "이미지";
            imgCell.appendChild(img);
            imgRow.appendChild(imgCell);
            table.appendChild(imgRow);

            // 추가 날짜 행
            table.appendChild(createDataRow('modal_addDate', '추가한 날짜 : ', data.bookShelfDTO.addDate));

            // 시작 날짜 행
            table.appendChild(createDataRow('modal_startDate', '시작한 날짜 : ', data.bookShelfDTO.startDate));

            // 목표 날짜 행
            table.appendChild(createDataRow('modal_targetDate', '목표 날짜 : ', data.bookShelfDTO.targetDate));

            // 완독 날짜 행
            table.appendChild(createDataRow('modal_endDate', '완독 날짜 : ', data.bookShelfDTO.endDate));

            // 진행 상황 행
            let progressRow = document.createElement('tr');
            let progressCell = document.createElement('td');
            progressCell.colSpan = "5";
            progressCell.className = "modal_page";
            progressCell.innerHTML = '<div class="progress-bar"><div class="progress"></div></div>' +
                '<div class="startPage">현재 : ' + data.bookShelfDTO.currentPage + '</div>' +
                '<div class="endPage">마지막페이지 : ' + data.bookDTO.itemPage + '</div>';
            progressRow.appendChild(progressCell);
            table.appendChild(progressRow);

            modalContent.appendChild(table);
            modalContent.addEventListener('click' ,(e)=>{
                if(e.target.id ===  closeBtnCell || e.target.closest('#closeBtnCell')) {
                    console.log("check2124");
                }
            })
        })
        .catch(error => console.error('Error:', error));
}

function createDataRow(className, labelText, date) {
    let row = document.createElement('tr');
    let cell = document.createElement('td');
    cell.colSpan = "4";
    cell.className = className;
    cell.textContent = labelText + (date ? moment(date).format('YYYY-MM-DD') : '설정하기');
    row.appendChild(cell);
    return row;
}
//다른 창 전부 막는용
function disableClickOutside() {
    // body 요소를 제외한 다른 부모 요소들의 클릭 이벤트를 막음
    document.querySelectorAll('body > *:not(#myModal)').forEach(function (element) {
        element.style.pointerEvents = "none";
    });
}

function enableClickOutside() {
    // body 요소를 제외한 다른 부모 요소들의 클릭 이벤트를 활성화
    document.querySelectorAll('body > *:not(#myModal)').forEach(function (element) {
        element.style.pointerEvents = "auto";
    });
}
