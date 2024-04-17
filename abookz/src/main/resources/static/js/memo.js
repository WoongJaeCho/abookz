// 현재 날짜와 시간을 가져오는 함수
function getCurrentDateTime() {
    var today = new Date();
    var year = today.getFullYear();
    var month = today.getMonth() + 1; // 월은 0부터 시작하므로 1을 더함
    var day = today.getDate();
    var hour = today.getHours();
    var minute = today.getMinutes();

    // 월과 일, 시간, 분이 한 자리 수일 경우 앞에 0을 추가하여 두 자리로 만듦
    if (month < 10) month = '0' + month;
    if (day < 10) day = '0' + day;
    if (hour < 10) hour = '0' + hour;
    if (minute < 10) minute = '0' + minute;

    return year + '-' + month + '-' + day + 'T' + hour + ':' + minute;
}

// 현재 시간 이후의 시간을 선택할 수 없도록 설정
function disableFutureTime() {
    var today = new Date();
    var hour = today.getHours();
    var minute = today.getMinutes();

    // 현재 시간을 기준으로 미래의 시간은 선택할 수 없도록 설정
    var maxTime = ('0' + hour).slice(-2) + ':' + ('0' + minute).slice(-2);
    var memoDateTimeInputs = document.querySelectorAll('.memoDateTime');
    memoDateTimeInputs.forEach(function(input) {
        input.setAttribute('max', maxTime);
    });
}

// 시작 날짜와 종료 날짜 input 요소에 현재 날짜와 시간을 기본값으로 설정하고, 현재 이후의 시간은 선택할 수 없도록 설정
document.addEventListener('DOMContentLoaded', function() {
    var memoDateTimeInputs = document.querySelectorAll('.memoDateTime');
    memoDateTimeInputs.forEach(function(input) {
        input.value = getCurrentDateTime(); // 현재 날짜와 시간을 기본값으로 설정
    });

    disableFutureTime(); // 현재 이후의 시간을 선택할 수 없도록 설정
});

// function validateForm() {
//     let page = document.querySelectorAll(".page");
//     let quotes = document.querySelectorAll(".quotes");
//     let note = document.querySelectorAll(".note");
//
//     if (!page[curIdx].value || !quotes[curIdx].value || !note[curIdx].value) {
//         alert("모든 필드를 작성해주세요.");
//         return false;
//     }
//     return true;
// }


let closeModal = document.querySelectorAll('.close-modal');
let memoModal = document.querySelectorAll("#memo_modal_inner");
let memoBtn = document.querySelectorAll('.memo_btn');
let overlay = document.querySelector('#overlay');


// closeModal[curIdx].addEventListener('click', ()=>{
//     memoModal[curIdx].style.display = "none";
//     overlay.style.display = "none";
// })
// console.log(curIdx);
// memoBtn[curIdx].addEventListener("click", ()=>{
//     console.log(memoBtn[curIdx]);
//     memoModal[curIdx].style.display = "block";
//     overlay.style.display = "block";
//     // memoModal.setAttribute("th:with", "idx=" + curIdx);
// })
// 각 요소에 대해 forEach를 사용하여 이벤트 리스너를 추가합니다.
closeModal.forEach((element, index) => {
    element.addEventListener('click', () => {
        memoModal[index].style.display = "none";
        overlay.style.display = "none";
    });
});

memoBtn.forEach((element, index) => {
    element.addEventListener("click", () => {
        memoModal[index].style.display = "block";
        overlay.style.display = "block";
    });
});