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


// document.getElementById('submitBtn').addEventListener('click', function() {
//     // note textarea의 값을 확인하여 빈 값이면 폼을 제출하지 않음
//     if (document.getElementById('note').value.trim() === '') {
//         alert('나의 생각을 입력해주세요.');
//         return;
//     }
//     document.getElementById('memoForm').submit();
// });

let closeModal = document.querySelector('.close-modal');
let memoModal = document.querySelector("#memo");
let memoBtn = document.querySelector('.memo_btn');
closeModal.addEventListener('click', ()=>{
    memoModal.style.display = "none";
})
memoBtn.addEventListener("click", ()=>{
    memoModal.style.display = "block";
})
