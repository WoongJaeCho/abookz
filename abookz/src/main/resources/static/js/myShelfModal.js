
let isInputCheck = false;
let modalAddDate = null;
let modalEndDate = null;
let modalStartDate = null;
let modalTargetDate = null;
function closeModal(modal) {

    modal.style.display = "none";
    modal.style.opacity = "0";
    enableClickOutside();
}
// 모달창
function openModal(modal, popup) {
    let value = popup.getAttribute('data-value');

    modal.style.display = "block";
    modal.style.opacity = "1";
    disableClickOutside();
    fetch("/modal?id=" + value, {
        method: "POST",
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ value: value })
    })
        .then(response => response.json())
        .then(data => {

            let table = document.createElement('table');
            table.className = 'modal-table';

            // 첵 제목 행
            let titleRow = document.createElement('tr');
            let titleCell = document.createElement('td');
            titleCell.colSpan = "5";
            titleCell.className = "modal_title";
            titleCell.textContent = "첵 제목: " + data.bookDTO.title;
            titleRow.appendChild(titleCell);
            const closeBtnCell = document.createElement('td');
            closeBtnCell.id = "myShelfClose";
            closeBtnCell.innerHTML = '<div id="myShelfModalClose" class="close-modal-button">X</div>';
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

            // 추가날짜
            table.appendChild(createDataRow('modal_addDate', '추가한 날짜 : ', data.bookShelfDTO.addDate));

            // 시작날짜
            table.appendChild(createDataRow('modal_startDate', '시작한 날짜 : ', data.bookShelfDTO.startDate));

            // 목표날짜
            table.appendChild(createDataRow('modal_targetDate', '목표 날짜 : ', data.bookShelfDTO.targetDate));

            // 완독날짜
            table.appendChild(createDataRow('modal_endDate', '완독 날짜 : ', data.bookShelfDTO.endDate));


            let progressRow = document.createElement('tr');
            let progressCell = document.createElement('td');
            progressCell.colSpan = "5";
            progressCell.id = "modal_page";
            progressCell.innerHTML =
                '<div id="progress-bar"><div id="progress">' +
                '<div id="modal-handle"></div></div></div>' +
                '<label class="startPage">읽은 페이지: <input type="number" id="currentPageInput" value="' + data.bookShelfDTO.currentPage + '" min="0" max="' + data.bookDTO.itemPage + '"></label>' +
                '<div class="endPage">마지막 페이지: ' + data.bookDTO.itemPage + '</div>';
            progressRow.appendChild(progressCell);
            table.appendChild(progressRow);
            let submitRow = document.createElement('tr');
            let submitCell = document.createElement('td');
            submitCell.colSpan ="2";
            submitCell.id = "modal_submit";
            submitCell.innerHTML= '<div class=submit-modal>설정하기</div>'
            submitRow.appendChild(submitCell);
            table.appendChild(submitRow);
            modalContent.appendChild(table);
            modalContent.style.pointerEvents = "auto";
            Array.from(modal.querySelectorAll('*')).forEach(element => {
                element.style.pointerEvents = "auto";
            });

            const closeBtn = document.getElementById('myShelfClose');
            closeBtn.addEventListener("click", function() {
                closeModal(modal);
                modalContent.removeChild(table);
            });
            const modalPage = document.getElementById('modal_page');
            const progressBar = document.getElementById('progress-bar').firstElementChild;
            const maxPage = data.bookDTO.itemPage;
            const currentPageInput = document.getElementById('currentPageInput');
            let currentPage = parseInt(currentPageInput.value, 10) || 0;
            const handle = document.getElementById('modal-handle');
            const progress = document.getElementById('progress-bar');
            let isDragging = false;
                let initialClickOffsetX = 0;
                const progressPercentage = (currentPage / maxPage) * 100;
                const maxProgressWidth = progress.offsetWidth;
                const xPosition = (progressPercentage / 100) * maxProgressWidth;
                progressBar.style.width = `${progressPercentage}%`;
                progressBar.style.backgroundColor = getColorBasedOnProgress(progressPercentage);
                handle.style.left = `${xPosition}px`;
                handle.addEventListener('mousedown', function(event) {
                    isDragging = true;
                    const handleCurrentLeft = parseInt(handle.style.left, 10) || 0;
                    initialClickOffsetX = event.clientX - handleCurrentLeft;
                    event.preventDefault();
                    updateProgress(handleCurrentLeft);
                    document.addEventListener('mousemove', mouseMoveHandler);
                    document.addEventListener('mouseup', stopDragging);
                });

                function stopDragging() {
                    isDragging = false;
                    document.removeEventListener('mousemove', mouseMoveHandler);
                    document.removeEventListener('mouseup', stopDragging);
                }
            function mouseMoveHandler(event) {
                if (!isDragging) return;
                const xPosition = event.clientX - initialClickOffsetX;
                updateProgress(xPosition);
            }
            function updateProgress(xPosition) {
                const maxProgressWidth = progress.offsetWidth;
                const newLeft = Math.min(Math.max(xPosition, 0), maxProgressWidth);
                const progressPercentage = (newLeft / maxProgressWidth) * 100;
                progressBar.style.width = `${progressPercentage}%`;
                progressBar.style.backgroundColor = getColorBasedOnProgress(progressPercentage);
                handle.style.left = `${newLeft}px`;
                updateCurrentPage(progressPercentage);
            }
            function getColorBasedOnProgress(progress) {
                if (progress < 25) {
                    return 'red';
                } else if (progress < 50) {
                    return 'orange';
                } else if (progress < 75) {
                    return 'yellow';
                } else {
                    return 'pink';
                }
            }

            function updateCurrentPage(progressPercentage) {
                const newPage = Math.round((progressPercentage / 100) * maxPage);
                currentPageInput.value = newPage;
                currentPage = newPage;
            }

            modalPage.addEventListener('input', function(event) {
                currentPage = parseInt(event.target.value, 10) || 0;
                if(currentPage > maxPage){
                    currentPage=maxPage;
                }else if(currentPage < 0){
                    currentPage = 0;
                }
                const progressPercentage = (currentPage / maxPage) * 100;
                progressBar.style.width = `${progressPercentage}%`;
                progressBar.style.backgroundColor = getColorBasedOnProgress(progressPercentage);
            });
            const submitModal = document.getElementById('modal_submit');
            submitModal.addEventListener('click', () => {
                changeDateModal(value,modalStartDate,modalAddDate,modalEndDate,modalTargetDate,currentPage);
            });

        })
        .catch(error => console.error('Error:', error));
}

function createDataRow(id, label, value) {
    var row = document.createElement('tr');
    var labelTd = document.createElement('td');
    labelTd.textContent = label;
    var valueTd = document.createElement('td');

    if( id ===  'modal_addDate'){
        var inputAddDate = document.createElement('input');
        inputAddDate.type = 'text';
        inputAddDate.className = 'date-picker start-date';
        inputAddDate.value = value ? moment(value).format('YYYY-MM-DD') : '설정하기';

        flatpickr(inputAddDate, {
            enableTime: false,
            dateFormat: "Y-m-d",
            minDate : 'today',
            onChange: function(selectedDates, dateStr, instance) {

                modalAddDate = dateStr;
            }
        });

        valueTd.appendChild(inputAddDate);
    }

    else if (id === 'modal_startDate') {
        var inputStartDate = document.createElement('input');
        inputStartDate.type = 'text';
        inputStartDate.className = 'date-picker start-date';
        inputStartDate.value = value ? moment(value).format('YYYY-MM-DD') : '설정하기';

        flatpickr(inputStartDate, {
            enableTime: false,
            dateFormat: "Y-m-d",
            minDate : 'today',
            onChange: function(selectedDates, dateStr, instance) {
                console.log("Selected date: ", dateStr);
                modalStartDate =dateStr;
            }
        });

        valueTd.appendChild(inputStartDate);
    } else if(id === 'modal_targetDate'){
        var inputTargetDate = document.createElement('input');
        inputTargetDate.type = 'text';
        inputTargetDate.className = 'date-picker target-date';
        inputTargetDate.value = value ? moment(value).format('YYYY-MM-DD') : '설정하기';

        flatpickr(inputTargetDate, {
            enableTime: false,
            dateFormat: "Y-m-d",
            minDate : 'today',
            onChange: function(selectedDates, dateStr, instance) {
                console.log("Selected date: ", dateStr);
                modalTargetDate = dateStr;
            }
        });

        valueTd.appendChild(inputTargetDate);
    }
    else if(id === 'modal_endDate'){
        var inputEndDate = document.createElement('input');
        inputEndDate.type = 'text';
        inputEndDate.className = 'date-picker end-date';
        inputEndDate.value = value ? moment(value).format('YYYY-MM-DD') : '설정하기';

        flatpickr(inputEndDate, {
            enableTime: false,
            dateFormat: "Y-m-d",
            minDate : 'today',
            onChange: function(selectedDates, dateStr, instance) {
                console.log("Selected date: ", dateStr);
                modalEndDate = dateStr;
            }
        });

        valueTd.appendChild(inputEndDate);
    }

    row.appendChild(labelTd);
    row.appendChild(valueTd);
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
function changeDateModal(value,start,add,end,target,current){
    let jsonData = {
        id : value ,
        currentPage : current
    };
    if (start !== null && start !== undefined) {
        jsonData.startDate = new Date(start);
    }

    if (add !== null && add !== undefined) {
        jsonData.addDate = new Date(add);
    }

    if (end !== null && end !== undefined) {
        jsonData.endDate = new Date(end);
    }
    if (target !== null && target !== undefined) {
        jsonData.targetDate = new Date(target);
    }

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
