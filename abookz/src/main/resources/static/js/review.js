document.addEventListener('DOMContentLoaded', function () {
  // 서버로부터 받은 별점 값을 기반으로 초기 선택 설정

  const initialRatingValue = document.getElementById('shelfGrade').value;
  console.log(initialRatingValue);
  setInitialRating(initialRatingValue);

  // 별점 클릭 이벤트 리스너 설정
  const ratings = document.querySelectorAll('.shelf-grade');
  ratings.forEach(function (rating) {
    rating.addEventListener('click', function () {
      const ratingValue = this.getAttribute('data-rating');
      submitRating(ratingValue);
    });
  });
});

// 초기 선택된 별점 설정
function setInitialRating(value) {
  const ratingInputs = document.querySelectorAll('.shelf-grade');
  ratingInputs.forEach(function(input) {
    if (input.getAttribute('data-rating') === value) {
      input.checked = true;
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
