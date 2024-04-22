var getIsbn13s = document.getElementsByClassName('getIsbn13');
var getButtons =document.getElementsByClassName('bookCategoryBtn');
console.log(getIsbn13s+ " = GETiSBN13")
for(var i=0; i<getButtons.length; i++){
    let getButton = getButtons[i];
    let getIsbn13 = getIsbn13s[i];
    getButton.addEventListener('click', function (){

        wantToReadCategory(getIsbn13)
    })
}

    function wantToReadCategory (isbn){
    let isbn13 = isbn.getAttribute('valueBook');
    console.log("isbn13"+isbn13);
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
                updateFeedback(message, false)
            });
    }
function updateFeedback(message, isSuccess ) {
    var headerElement = document.getElementById('header');
    let feedbackElement = document.getElementById('rating-feedback');
    if (!feedbackElement) {
        feedbackElement = document.createElement('div');
        feedbackElement.id = 'rating-feedback';
        headerElement.appendChild(feedbackElement); // 수정된 부분
    }

    if (isSuccess) {
        // 성공 메시지 스타일
        feedbackElement.innerHTML = `
      <div role="alert" class="alert alert-success z-50">
        <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
        <span>${message}</span>
      </div>
    `;
    } else {
        // 실패 메시지 스타일
        feedbackElement.innerHTML = `
      <div role="alert" class="alert alert-error z-50">
        <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
        <span>${message}</span>
      </div>
    `;
    }
    // 몇 초 후 메시지 숨기기
    setTimeout(() => {
        feedbackElement.innerHTML = ''; // 메시지 숨김
    }, 3000); // 3초 후 메시지 숨김
}