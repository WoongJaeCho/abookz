function loadReviews(pageNumber, query,sort) {
  const ISBN13 = document.getElementById('ISBN13').value;

  fetch(`/review/reviews?pageNumber=${pageNumber}&ISBN13=${ISBN13}&query=${encodeURIComponent(query)}&sort=${encodeURIComponent(sort)}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
      .then(response => response.json())
      .then(data => {
        updateReviewSection(data.reviews);  // 리뷰 섹션 업데이트
        updatePagination(data.currentPage, data.totalPages,query,sort);  // 페이징 정보 업데이트
      })
      .catch(error => console.error('Error loading the reviews:', error));
}

function updateReviewSection(data) {
  const reviewContainer = document.getElementById('review_detail_content');
  reviewContainer.innerHTML = '';  // 기존 리뷰 내용을 비움

  // 새로운 리뷰 내용을 생성하여 페이지에 추가
  data.forEach(review => {
    let tagDescription;

    const likeText =  review.likesCount  >= 2  ? 'Likes' : 'Like';
    // 스포일러 여부에 따른 클래스와 버튼 추가
    const spoilerClass = review.isSpoilerActive ? 'blur-lg' : '';
    const spoilerBtnDisabled = review.isSpoilerActive ? 'btn-disabled' : '';
    let likeButtonClass = review.liked ? 'liked fill-current' : '';    console.log(review.liked);

    const spoilerButton = review.isSpoilerActive ?

        ` <div class="spoiler-alert text-center p-4 absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white rounded-lg shadow-2xl" id="spoiler-alert-${review.id}">
    <h2 class="text-xl font-semibold text-red-600">스포일러 주의!</h2>
    <button class="btn mt-4 btn-sm btn-accent rounded-full bg-blue-500 text-white hover:bg-blue-600 focus:outline-none focus:ring focus:ring-blue-300 transition ease-in-out duration-300" onclick="revealSpoiler(this, '${review.id}')">
        리뷰 내용 보기
    </button>
</div>` : '';
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
    reviewElement.className = 'space-y-4 relative';
    reviewElement.innerHTML = `
              <div id="review-${review.id}" class="review bg-white shadow-lg rounded-lg overflow-hidden ${spoilerClass}">
           

      <input type="hidden" class="shelfGrade" value="${review.bookShelfDTO.bookShelfGrade}" />
            <div class="flex w-full ">
              <div class="w-1/4 p-4">
                <img class="profile_img w-24 h-24 object-cover rounded-full mx-auto"
                     src="${review.bookShelfDTO.memberDTO.profile}"
                     alt="프로필이미지">
                <div class="text-center mt-2" >${review.bookShelfDTO.memberDTO.name}</div>
              </div>
              <div class="w-3/4 p-4">
                <div class="flex ">
                 <div class="rating rating-md rating-half">
                                                <input type="radio" name="rating-${review.id}" class="rating-hidden" data-rating="0" />
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
                <div class="text-start mb-4">${review.content}</div>
                <div class="flex justify-end space-x-4">
                  <div class="flex items-center">
                    <button class="btn-like btn btn-sm btn-outline btn-accent ${likeButtonClass} ${spoilerBtnDisabled}" onclick="clickLike(this,${review.id})"><svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                      <path stroke-linecap="round" stroke-linejoin="round" d="M6.633 10.25c.806 0 1.533-.446 2.031-1.08a9.041 9.041 0 0 1 2.861-2.4c.723-.384 1.35-.956 1.653-1.715a4.498 4.498 0 0 0 .322-1.672V2.75a.75.75 0 0 1 .75-.75 2.25 2.25 0 0 1 2.25 2.25c0 1.152-.26 2.243-.723 3.218-.266.558.107 1.282.725 1.282m0 0h3.126c1.026 0 1.945.694 2.054 1.715.045.422.068.85.068 1.285a11.95 11.95 0 0 1-2.649 7.521c-.388.482-.987.729-1.605.729H13.48c-.483 0-.964-.078-1.423-.23l-3.114-1.04a4.501 4.501 0 0 0-1.423-.23H5.904m10.598-9.75H14.25M5.904 18.5c.083.205.173.405.27.602.197.4-.078.898-.523.898h-.908c-.889 0-1.713-.518-1.972-1.368a12 12 0 0 1-.521-3.507c0-1.553.295-3.036.831-4.398C3.387 9.953 4.167 9.5 5 9.5h1.053c.472 0 .745.556.5.96a8.958 8.958 0 0 0-1.302 4.665c0 1.194.232 2.333.654 3.375Z" />
                      </svg>
                      <span class="">${review.likesCount} ${likeText}</spanc>
                    </button>
                  </div>
                  <div class="flex items-center">
                    <label class="btn-comment btn btn-sm btn-outline btn-info ml-2 ${spoilerBtnDisabled}" onclick="toggleFill(this, 'fill-current')"> <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 0 1 .865-.501 48.172 48.172 0 0 0 3.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0 0 12 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018Z" />
                    </svg>
                    <span class="">Comment</span></label>
                  </div>
                </div>
                <div th:replace="~{review/comment :: comment}" />
              </div>
            </div>
          </div>
          </div>
           ${spoilerButton}
    `;
    reviewContainer.appendChild(reviewElement);
  });
  setInitialRating();
  setInitialLike()


}

function updatePagination(currentPage, totalPages,query,sort) {
  const paginationContainer = document.getElementById('pagination');
  paginationContainer.innerHTML = '';

  if (currentPage > 0) {
    const prevButton = document.createElement('button');
    prevButton.innerText = 'Previous';
    prevButton.onclick = () => loadReviews(currentPage - 1,query,sort);
    paginationContainer.appendChild(prevButton);
  }

  for (let i = 0; i < totalPages; i++) {
    const pageButton = document.createElement('button');
    pageButton.innerText = i + 1;
    pageButton.className = currentPage === i ? 'btn btn-active' : 'btn';
    pageButton.onclick = () => loadReviews(i,query,sort);
    paginationContainer.appendChild(pageButton);
  }

  if (currentPage < totalPages - 1) {
    const nextButton = document.createElement('button');
    nextButton.innerText = 'Next';
    nextButton.onclick = () => loadReviews(currentPage + 1,query,sort);
    paginationContainer.appendChild(nextButton);
  }
}


function setInitialRating() {
  const reviews = document.querySelectorAll('.review'); // 모든 리뷰 컨테이너 선택
  reviews.forEach(review => {
    const gradeInput = review.querySelector('.shelfGrade'); // 각 리뷰의 평점 입력 필드 선택
    const grade = parseFloat(gradeInput.value);
    console.log(gradeInput);
    console.log(grade);
      const ratingInputs = review.querySelectorAll(`input[type="radio"][data-rating="${grade}"]`); // 해당 평점과 일치하는 라디오 버튼 선택
      if (ratingInputs.length > 0) {
        ratingInputs[0].checked = true; // 해당 라디오 버튼을 체크 상태로 설정
      }
  });
}


function setInitialLike() {
  const reviews = document.querySelectorAll('.review'); // 모든 리뷰 컨테이너 선택
  reviews.forEach(review => {
    const likeButton = review.querySelector('.btn-like'); // 해당 리뷰의 좋아요 버튼을 찾습니다.
    if (likeButton.classList.contains('liked')) {
      toggleFill(likeButton,'fill-current');
    }else{
      toggleFill(likeButton,'fill-none');
    }
  });
}

document.addEventListener("DOMContentLoaded", function () {
  loadReviews(0,"","최신순");
});

function toggleFill(element, fillClass) {
  const svg = element.querySelector('svg');

  // 'fill-current' 클래스를 처리
  if (fillClass === 'fill-current') {
    if (!svg.classList.contains('fill-current')) {
      svg.classList.add('fill-current');     // 'fill-current' 클래스 추가
      svg.classList.remove('fill-none');     // 'fill-none' 클래스 제거
      svg.style.fill = 'currentColor';       // fill 속성을 'currentColor'로 설정
    }
  }
  // 'fill-none' 클래스를 처리
  else if (fillClass === 'fill-none') {
    if (!svg.classList.contains('fill-none')) {
      svg.classList.add('fill-none');        // 'fill-none' 클래스 추가
      svg.classList.remove('fill-current');  // 'fill-current' 클래스 제거
      svg.style.fill = 'none';               // fill 속성을 'none'으로 설정
    }
  }
}

function revealSpoiler(button, reviewId) {
  const review = document.getElementById(`review-${reviewId}`);

  button.parentElement.remove();
  review.classList.remove('blur-lg'); // 리뷰에서 Blur 효과 제거

  // 좋아요 및 코멘트 버튼 활성화
  const likeButton = review.querySelector('.btn-like');
  const commentButton = review.querySelector('.btn-comment');
  if (likeButton && commentButton) {
    likeButton.classList.remove('btn-disabled');
    commentButton.classList.remove('btn-disabled');
  }
}

function clickLike(button, reviewId) {
  fetch(`/like/${reviewId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer your-token-here'  // 토큰 교체
    },
    body: JSON.stringify({ like: true })
  })
      .then(response => {
        if (!response.ok) {
          if (response.status === 401) {
            // 권한 없음 오류 처리
            throw new Error('로그인이 필요합니다.');
          } else {
            // 기타 네트워크 오류 처리
            throw new Error('Network response was not ok');
          }
        }

        return response.json();
      })
      .then(data => {
        // 좋아요 상태 및 수 업데이트
        const likeText = data.likesCount >= 2 ? 'Likes' : 'Like';
        button.querySelector('span').textContent = `${data.likesCount} ${likeText}`;
        if (data.liked) {
          button.classList.add('liked');
          toggleFill(button, 'fill-current');
        } else {
          button.classList.remove('liked');
          toggleFill(button, 'fill-none');
        }
      })
      .catch(error => {
        console.error('Error:', error);
        alert('Error: ' + error.message);
      });
}

function searchReviews() {
  const query = document.getElementById('searchQuery').value;
  const sort = document.getElementById('review_sort').value; // 정렬 값 추출
  loadReviews(0, query,sort);  // 페이지 로딩 함수에 검색어 파라미터 추가
}

