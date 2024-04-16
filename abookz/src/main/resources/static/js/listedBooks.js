function setInitialRating(value) {
  const cards  = document.querySelectorAll('.card-body'); // 모든 리뷰 컨테이너 선택
  cards.forEach(card => {
    const grade = parseFloat(card.querySelector('.shelfGrade').value); // 각 리뷰의 평점 값을 가져옴
    const ratingInputs =card.querySelectorAll(`input[type="radio"][data-rating="${grade}"]`); // 해당 평점과 일치하는 라디오 버튼 선택
    if (ratingInputs.length > 0) {
      ratingInputs[0].checked = true; // 해당 라디오 버튼을 체크 상태로 설정
    }
  });
}

document.addEventListener("DOMContentLoaded", function () {
  setInitialRating();
});