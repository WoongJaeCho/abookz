document.addEventListener("DOMContentLoaded", function() {
    var bookRating = document.getElementById('bookDetailGrade').getAttribute('grade');
    var notRating = document.getElementById('notRating');
    var ratingInputs = document.querySelectorAll('.aladinStar');
    var bookRatingHalf = 0; // 초기값을 0으로 설정하여 `null`인 경우 처리
    var checkedCount = 0; // 체크된 입력 수를 추적

    if (bookRating !== null) {
        bookRatingHalf = parseFloat(bookRating) / 2;
    }


    ratingInputs.forEach(input => {
        if (parseFloat(input.getAttribute('data-rating')) <= bookRatingHalf) {
            input.checked = true;
            checkedCount++;
        }
    });
    if (checkedCount === 0) {
        notRating.checked = true;
    }

});