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
        updateRatingFeedback(data.message);
      })
      .catch((error) => {
        console.error('Error:', error);
        updateRatingFeedback('별점 저장에 실패하였습니다. 다시 시도해주세요.');
      });
}

// 피드백 메시지 업데이트
function updateRatingFeedback(message) {
  let feedbackElement = document.getElementById('rating-feedback');
  if (!feedbackElement) {
    feedbackElement = document.createElement('div');
    feedbackElement.id = 'rating-feedback';
    document.body.appendChild(feedbackElement);
  }
  feedbackElement.textContent = message;
}
