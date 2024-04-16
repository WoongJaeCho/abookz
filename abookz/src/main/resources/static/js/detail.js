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
 function wantToRead (isbn){
    var isbn13 = isbn;
   fetch('/want?book=' + isbn13,{
    method: 'POST',
    headers: {
     'Content-Type' : 'application/json'
    },
    body: JSON.stringify({isbn13 : isbn13})
   })
       .then(response => {
           if (!response.ok) {
               throw new Error('Network response was not ok');
           }
           return response.text();
       })
       .then(data => {
           alert(data);
       })
       .catch(error => {
           console.error('There was a problem with the fetch operation:', error);
           alert("이미 등록 되어있습니다");

       });
 }
