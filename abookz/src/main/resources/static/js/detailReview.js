function loadReviews(pageNumber) {
  // const sort = document.querySelector('.select select-bordered').value; // 정렬 옵션 가져오기
  const bookContainer = document.getElementById('review_detail_content');
  const bookId = bookContainer.getAttribute('data-book-id');

  // fetch(`/review/reviews?pageNumber=${pageNumber}&sort=${sort}&bookId=${bookId}`, {
  fetch(`/review/reviews?pageNumber=${pageNumber}&bookId=${bookId}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
      .then(response => response.json())
      .then(data => {
        updateReviewSection(data.reviews);  // 리뷰 섹션 업데이트
        updatePagination(data.currentPage, data.totalPages);  // 페이징 정보 업데이트
      })
      .catch(error => console.error('Error loading the reviews:', error));
}

function updateReviewSection(data) {
  const reviewContainer = document.getElementById('review_detail_content');
  reviewContainer.innerHTML = '';  // 기존 리뷰 내용을 비움

  // 새로운 리뷰 내용을 생성하여 페이지에 추가
  data.forEach(review => {
    let tagDescription;

// review.bookShelfDTO.tag 값에 따라 한국어 설명을 할당합니다.
    switch (review.bookShelfDTO.tag) {
      case "READ":
        tagDescription = "읽은 책";
        break;
      case "WANT_TO_READ":
        tagDescription = "읽고 싶은 책";
        break;
      case "CURRENTLY_READING":
        tagDescription = "읽고 있는 책";
        break;
    }
    const reviewElement = document.createElement('div');
    reviewElement.className = 'space-y-4';
    reviewElement.innerHTML = `
              <div  class="review bg-white shadow-lg rounded-lg overflow-hidden">

      <input type="hidden" class="shelfGrade" value="${review.bookShelfDTO.bookShelfGrade}" />
            <div class="flex w-full">
              <div class="w-1/4 p-4">
                <img class="profile_img w-24 h-24 object-cover rounded-full mx-auto"
                     src="${review.bookShelfDTO.memberDTO.profile}"
                     alt="프로필이미지">
                <div class="text-center mt-2" >${review.bookShelfDTO.memberDTO.name}</div>
              </div>
              <div class="w-3/4 p-4">
                <div class="flex ">
                 <div class="rating rating-md rating-half">
                    <!--                            <input type="radio" name="rating-10" class="rating-hidden" />-->
                    <input type="radio" name="rating-${review.id}" class="pointer-events-none shelf-grade bg-yellow-500 mask mask-star-2 mask-half-1" data-rating="0.5"/>
                    <input type="radio" name="rating-${review.id}" class="pointer-events-none shelf-grade bg-yellow-500 mask mask-star-2 mask-half-2" data-rating="1.0"/>
                    <input type="radio" name="rating-${review.id}" class="pointer-events-none shelf-grade bg-yellow-500 mask mask-star-2 mask-half-1" data-rating="1.5"/>
                    <input type="radio" name="rating-${review.id}" class="pointer-events-none shelf-grade bg-yellow-500 mask mask-star-2 mask-half-2" data-rating="2.0"/>
                    <input type="radio" name="rating-${review.id}" class="pointer-events-none shelf-grade bg-yellow-500 mask mask-star-2 mask-half-1" data-rating="2.5"/>
                    <input type="radio" name="rating-${review.id}" class="pointer-events-none shelf-grade bg-yellow-500 mask mask-star-2 mask-half-2" data-rating="3.0" />
                    <input type="radio" name="rating-${review.id}" class="pointer-events-none shelf-grade bg-yellow-500 mask mask-star-2 mask-half-1" data-rating="3.5" />
                    <input type="radio" name="rating-${review.id}" class="pointer-events-none shelf-grade bg-yellow-500 mask mask-star-2 mask-half-2" data-rating="4.0" />
                    <input type="radio" name="rating-${review.id}" class="pointer-events-none shelf-grade bg-yellow-500 mask mask-star-2 mask-half-1" data-rating="4.5" />
                    <input type="radio" name="rating-${review.id}" class="pointer-events-none shelf-grade bg-yellow-500 mask mask-star-2 mask-half-2" data-rating="5.0" />
                  </div>
                </div>
                <div class="flex justify-between items-center mb-4">
                  <div>${tagDescription}</div>
                  <div>${review.createdDate}</div>
                </div>
                <div class="mb-4">${review.content}</div>
                <div class="flex justify-end space-x-4">
                  <div class="flex items-center">
                    <button class="btn btn-sm btn-outline btn-accent">Like</button>
                  </div>
                  <div class="flex items-center">
                    <label class="btn btn-sm btn-outline btn-info ml-2">comment</label>
                  </div>
                </div>
                <div th:replace="~{review/comment :: comment}" />
              </div>
            </div>
          </div>
    `;
    reviewContainer.appendChild(reviewElement);
  });
  setInitialRating();



}

function updatePagination(currentPage, totalPages) {
  const paginationContainer = document.getElementById('pagination');
  paginationContainer.innerHTML = '';

  if(currentPage > 0) {
    const prevButton = document.createElement('button');
    prevButton.innerText = 'Previous';
    prevButton.onclick = () => loadReviews(currentPage - 1);
    paginationContainer.appendChild(prevButton);
  }

  for(let i = 0; i < totalPages; i++) {
    const pageButton = document.createElement('button');
    pageButton.innerText = i + 1;
    pageButton.className = currentPage === i ? 'btn btn-active' : 'btn';
    pageButton.onclick = () => loadReviews(i);
    paginationContainer.appendChild(pageButton);
  }

  if(currentPage < totalPages - 1) {
    const nextButton = document.createElement('button');
    nextButton.innerText = 'Next';
    nextButton.onclick = () => loadReviews(currentPage + 1);
    paginationContainer.appendChild(nextButton);
  }
}


function setInitialRating(value) {
  const reviews = document.querySelectorAll('.review'); // 모든 리뷰 컨테이너 선택
  reviews.forEach(review => {
    const grade = parseFloat(review.querySelector('.shelfGrade').value); // 각 리뷰의 평점 값을 가져옴
    const ratingInputs = review.querySelectorAll(`input[type="radio"][data-rating="${grade}"]`); // 해당 평점과 일치하는 라디오 버튼 선택
    if (ratingInputs.length > 0) {
      ratingInputs[0].checked = true; // 해당 라디오 버튼을 체크 상태로 설정
    }
  });
}

document.addEventListener("DOMContentLoaded", function() {
  setInitialRating();
});