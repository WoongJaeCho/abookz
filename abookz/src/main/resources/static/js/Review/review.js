document.addEventListener('DOMContentLoaded', function () {
  // 서버로부터 받은 별점 값을 기반으로 초기 선택 설정

  const initialRatingValue = document.getElementById('shelfGrade').value;
  console.log(initialRatingValue);
  setInitialRating(initialRatingValue);

  // 별점 클릭 이벤트 리스너 설정
  const shelfRatingContainer = document.querySelector('.shelf-rating');
  const ratingInputs = shelfRatingContainer.querySelectorAll('input[type="radio"]');
  ratingInputs.forEach(function (input) {
    input.addEventListener('click', function () {
      const ratingValue = this.getAttribute('data-rating');
      submitRating(ratingValue);
    });
  });
});

function setInitialRating(value) {
  const shelfRatingContainer = document.querySelector('.shelf-rating');
  const ratingInputs = shelfRatingContainer.querySelectorAll('input[type="radio"]');  // 모든 라디오 버튼을 선택
  const hiddenRatingInput = shelfRatingContainer.querySelector('.rating-hidden');

  ratingInputs.forEach(function(input) {
    // data-rating 속성이 주어진 value와 일치하는지 확인
    if (input.getAttribute('data-rating') === value) {
      hiddenRatingInput.checked = false;
      input.checked = true;  // 조건을 만족하는 라디오 버튼을 체크
    }
  });
}

// 별점 서버 제출 및 피드백 업데이트
function submitRating(ratingValue) {
  const path = window.location.pathname;
  const segments = path.split('/');
  const bookShelfId = segments[2];
  const bookId = segments[3];

  fetch('/review/rating', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      bookShelfId: bookShelfId,
      bookId: bookId,
      rating: ratingValue,
    })
  })
      .then(response => response.json())
      .then(data => {
        console.log('Success:', data.message);
        updateFeedback(data.message, true);
        // updateRatingFeedback('별점 저장에 실패하였습니다. 다시 시도해주세요.', false);//실패 테스트용
      })
      .catch(error => {
        console.error('Error:', error);
        updateFeedback('별점 저장에 실패하였습니다. 다시 시도해주세요.', false);
      });
}

// 피드백 메시지 업데이트
function updateFeedback(message, isSuccess ) {
  console.log("updatefeedback");
  let feedbackElement = document.getElementById('rating-feedback');
  if (!feedbackElement) {
    feedbackElement = document.createElement('div');
    feedbackElement.id = 'rating-feedback';
    document.body.appendChild(feedbackElement);
  }

  if (isSuccess) {
    // 성공 메시지 스타일
    feedbackElement.innerHTML = `
      <div role="alert" class="alert alert-success">
        <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
        <span>${message}</span>
      </div>
    `;
  } else {
    // 실패 메시지 스타일
    feedbackElement.innerHTML = `
      <div role="alert" class="alert alert-error">
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
