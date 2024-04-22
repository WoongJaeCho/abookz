document.addEventListener('DOMContentLoaded', function () {
  var checkbox = document.getElementById('collapse-check');

  checkbox.addEventListener('click', function () {
    loadReviews(0, "", "최신순");
  });
});

function loadReviews(pageNumber, query, sort) {

  const collapseDiv = document.querySelector('.collapse'); // 타겟이 되는 div 요소 선택

  const ISBN13 = document.getElementById('ISBN13').value;
  fetch(`/review/reviews?pageNumber=${pageNumber}&ISBN13=${ISBN13}&query=${encodeURIComponent(query)}&sort=${encodeURIComponent(sort)}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
      .then(response => response.json())
      .then(data => {
        if (data.reviews.length === 0) {
          const modal = document.getElementById('review-modal');
          modal.showModal();
        } else {
          collapseDiv.classList.add('collapse-open');
          updateReviewSection(data.reviews);  // 리뷰 섹션 업데이트
          updatePagination(data.currentPage, data.totalPages, query, sort);  // 페이징 정보 업데이트
        }
      })
      .catch(error => updateFeedback(error.message, false));

}

function updateReviewSection(data) {
  const reviewContainer = document.getElementById('review_detail_content');

  reviewContainer.innerHTML = '';
  data.forEach(review => {
    const reviewElement = createReviewElement(review);
    reviewContainer.appendChild(reviewElement);
    console.log(`Review ID for comment section: ${review.id}`); // Review ID 확인
    createCommentSection(review.id); // 댓글 컨테이너 초기화 호출

  });
  setInitialRating();
  setInitialLike();

}

function createSpoilerButton(review) {
  return review.isSpoilerActive ?
      `<div class="spoiler-alert text-center p-4 absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white rounded-lg shadow-2xl" id="spoiler-alert-${review.id}">
      <h2 class="text-xl font-semibold text-red-600">스포일러 주의!</h2>
      <button class="btn mt-4 btn-sm btn-accent rounded-full bg-blue-500 text-white hover:bg-blue-600 focus:outline-none focus:ring focus:ring-blue-300 transition ease-in-out duration-300" onclick="revealSpoiler(this, '${review.id}')">
        리뷰 내용 보기
      </button>
    </div>` : '';


}

setInitialRating();


setInitialLike()

function updatePagination(currentPage, totalPages, query, sort) {
  const paginationContainer = document.getElementById('pagination');

  paginationContainer.innerHTML = '';
  if (currentPage > 0) {
    const prevButton = document.createElement('button');
    prevButton.innerText = 'Previous';
    prevButton.onclick = () => loadReviews(currentPage - 1, query, sort);
    paginationContainer.appendChild(prevButton);

  }
  for (let i = 0; i < totalPages; i++) {
    const pageButton = document.createElement('button');
    pageButton.innerText = i + 1;
    pageButton.className = currentPage === i ? 'btn btn-active' : 'btn';
    pageButton.onclick = () => loadReviews(i, query, sort);
    paginationContainer.appendChild(pageButton);

  }
  if (currentPage < totalPages - 1) {
    const nextButton = document.createElement('button');
    nextButton.innerText = 'Next';
    nextButton.onclick = () => loadReviews(currentPage + 1, query, sort);
    paginationContainer.appendChild(nextButton);
  }


}

function setInitialRating() {
  document.querySelectorAll('.review').forEach(review => {
    const ratingValue = parseFloat(review.querySelector('.shelfGrade').value).toFixed(1);
    const ratingInput = review.querySelector(`input[type="radio"][data-rating="${ratingValue}"]`);
    if (ratingInput) {
      ratingInput.checked = true;
    }
  });

}

function setInitialLike() {
  document.querySelectorAll('.btn-like').forEach(button => {
    if (button.classList.contains('liked')) {
      toggleFill(button, 'fill-current');
    } else {
      toggleFill(button, 'fill-none');
    }
  });

}


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
    },
    body: JSON
  })
      .then(response => {
        if (!response.ok) {
          if (response.status === 401) {
            throw new Error('로그인이 필요합니다.');
          } else {
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
        updateFeedback(error.message, false)

      });
}

function searchReviews() {
  const query = document.getElementById('searchQuery').value;
  const sort = document.getElementById('review_sort').value; // 정렬 값 추출
  loadReviews(0, query, sort);  // 페이지 로딩 함수에 검색어 파라미터 추가
}

function updateCommentSection(reviewId, newComment) {
  const commentsContainer = document.getElementById(`comments-for-review-${reviewId}`);
  const commentsList = commentsContainer.querySelector(".flex.flex-col.gap-5.m-3");

  // 새로운 댓글 엘리먼트 생성
  const commentElement = createCommentElement(newComment);
  commentsList.appendChild(commentElement);

  // 댓글 입력 폼을 댓글 목록의 끝으로 이동
  const commentForm = commentsContainer.querySelector('.w-full.px-3.mb-2.mt-6');
  commentsContainer.appendChild(commentForm);
}

function createReviewElement(review) {
  let tagDescription;

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
  const spoilerClass = review.isSpoilerActive ? 'blur-lg' : '';
  const spoilerBtnDisabled = review.isSpoilerActive ? 'btn-disabled' : '';
  const likeButtonClass = review.liked ? 'liked fill-current' : '';
  const likeText = review.likesCount >= 2 ? 'Likes' : 'Like';
  const commentText = review.commentsCount >= 2 ? 'Comments' : 'Comment';

  const spoilerButton = createSpoilerButton(review); // Generate the spoiler button HTML

  const reviewElement = document.createElement('div');
  reviewElement.className = 'space-y-4 relative review';
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
                      <span class="like-${review.id}">${review.likesCount} ${likeText}</spanc>
                    </button>
                  </div>
                  <div class="flex items-center">
                    <label class="btn-comment btn btn-sm btn-outline btn-info ml-2 ${spoilerBtnDisabled}" onclick="toggleComments(this, '${review.id}')">
                        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 fill-none">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M7.5 8.25h9m-9 3H12m-9.75 1.51c0 1.6 1.123 2.994 2.707 3.227 1.129.166 2.27.293 3.423.379.35.026.67.21.865.501L12 21l2.755-4.133a1.14 1.14 0 0 1 .865-.501 48.172 48.172 0 0 0 3.423-.379c1.584-.233 2.707-1.626 2.707-3.228V6.741c0-1.602-1.123-2.995-2.707-3.228A48.394 48.394 0 0 0 12 3c-2.392 0-4.744.175-7.043.513C3.373 3.746 2.25 5.14 2.25 6.741v6.018Z" />
                      </svg>
              <span>${review.commentsCount} ${commentText}</span>
                    </label>
                  </div>
                </div>
          <div id="comment-${review.id}" class="comment-section"></div>
              </div>
            </div>
          </div>
          </div>
           ${spoilerButton}
    `;

  return reviewElement;

}


function createCommentSection(reviewId) {
  const reviewElement = document.getElementById(`review-${reviewId}`);
  if (!reviewElement) {
    console.error('Review element not found for review:', reviewId);
    return;
  }

  // 댓글 컨테이너 생성 및 설정
  const commentContainer = document.createElement('div');
  commentContainer.id = `comments-for-review-${reviewId}`;
  commentContainer.className = "comments-container w-[720px] bg-white rounded-lg border p-1 md:p-3 m-10";
  commentContainer.style.display = 'none'; // 초기에는 숨김

  const commentsList = document.createElement('div');
  commentsList.className = "flex flex-col gap-5 m-3";
  commentContainer.appendChild(commentsList);

  // "더보기" 버튼 생성
  const moreCommentsButton = document.createElement('button');
  moreCommentsButton.innerText = '더보기';
  moreCommentsButton.onclick = () => loadMoreComments(reviewId);
  moreCommentsButton.className = ("btn btn-block hidden")

  moreCommentsButton.id = `more-comments-button-${reviewId}`; // 클래스 추가

  const commentForm = document.createElement('div');
  commentForm.className = "w-full px-3 mb-2 mt-6 hidden";
  commentForm.innerHTML = `
    <textarea id="comment-input-${reviewId}" class="bg-gray-100 rounded border border-gray-400 leading-normal resize-none w-full h-20 py-2 px-3 font-medium placeholder-gray-400 focus:outline-none focus:bg-white" name="body" placeholder="댓글입력" required></textarea>
    <div class="w-full flex justify-end px-3 my-3">
      <button class="btn px-2.5 py-1.5 rounded-md text-white text-sm bg-indigo-500 text-lg" onclick="submitComment(${reviewId})">댓글등록</button>
    </div>`;
  // 댓글과 "더보기" 버튼을 DOM에 추가

  reviewElement.appendChild(commentContainer);
  reviewElement.appendChild(moreCommentsButton);
  reviewElement.appendChild(commentForm);


}

function loadComments(reviewId, pageNumber) {
  const commentsContainer = document.getElementById(`comments-for-review-${reviewId}`);
  const moreCommentsButton = document.getElementById(`more-comments-button-${reviewId}`);

  fetch(`/comment/${reviewId}?page=${pageNumber}&size=5`)  // 페이지당 댓글 수를 5개로 제한
      .then(response => response.json())
      .then(data => {
        if (data.comments.length > 0) {
          data.comments.forEach(comment => {
            const commentElement = createCommentElement(comment);
            commentsContainer.appendChild(commentElement);
          });
          commentsContainer.setAttribute('data-current-page', pageNumber);  // 현재 페이지 저장
        }

        // "더 보기" 버튼 상태 업데이트
        moreCommentsButton.style.display = data.hasNext ? 'block' : 'none';
      })
      .catch(error => updateFeedback(error.message, false));
}

function loadMoreComments(reviewId) {
  const commentsContainer = document.getElementById(`comments-for-review-${reviewId}`);
  let currentPage = parseInt(commentsContainer.getAttribute('data-current-page') || '0');
  loadComments(reviewId, currentPage + 1);  // 다음 페이지 로드
}


function submitComment(reviewId) {
  const commentInput = document.getElementById('comment-input-' + reviewId);
  const comment = commentInput.value.trim(); // 공백 제거

  fetch(`/comment/${reviewId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({comment: comment}) // JSON 형식으로 변환
  })
      .then(response => {
        if (!response.ok) {
          // 오류가 있을 경우, 오류를 처리하는 코드 필요
          return response.json().then(err => {
            throw new Error(err.message);  // 서버 에러 메시지를 던짐
          });
        }
        return response.json(); // 응답을 JSON으로 변환
      })
      .then(data => {
        updateCommentSection(reviewId, data.comment);
        commentInput.value = ''; // 입력 필드 초기화
      })
      .catch(error => {
        if (error.message === "로그인이 필요합니다.") {
          updateFeedback(error.message, false);
        }
      });
}


function toggleComments(button, reviewId) {
  const commentsContainer = document.getElementById(`comments-for-review-${reviewId}`);
  const moreButton = document.getElementById(`more-comments-button-${reviewId}`);
  document.querySelector(`#comment-input-${reviewId}`).parentElement.classList.remove("hidden")

  // 댓글 컨테이너의 표시 상태 토글
  if (commentsContainer.style.display === 'none' || !commentsContainer.style.display) {
    commentsContainer.style.display = 'block';  // 댓글 보이기
    button.querySelector('svg').classList.add('fill-current');
    button.querySelector('svg').classList.remove('fill-none');

    if (!commentsContainer.hasAttribute('data-loaded')) {
      fetchComments(reviewId, commentsContainer);
      commentsContainer.setAttribute('data-loaded', 'true'); // 데이터 로드됨 표시
    }
    moreButton.classList.remove('hidden');
  } else {
    commentsContainer.style.display = 'none';  // 댓글 숨기기
    button.querySelector('svg').classList.add('fill-none');
    button.querySelector('svg').classList.remove('fill-current');
    moreButton.classList.add('hidden');
  }
}


let currentCommentPage = 0;

function fetchComments(reviewId, container, nextPage = false) {
  if (nextPage) {
    currentCommentPage++;  // 다음 페이지로 이동
  }

  fetch(`/comment/${reviewId}?page=${currentCommentPage}&size=5`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json'
    }
  })
      .then(response => response.json())
      .then(data => {
        appendComments(data.comments, container);  // 댓글 추가
        if (data.hasNext) {
          document.getElementById(`more-comments-button-${reviewId}`).classList.remove('hidden');  // 다음 페이지가 있으면 버튼 표시
        } else {
          document.getElementById(`more-comments-button-${reviewId}`).classList.add('hidden');  // 다음 페이지가 없으면 버튼 숨김
        }
      })
      .catch(error => updateFeedback(error.message, false));
}


function appendComments(comments, container) {
  comments.forEach(comment => {
    const commentElement = createCommentElement(comment);
    container.appendChild(commentElement);
  });
}


var stompClient = null;

function connect() {
  var socket = new SockJS('/ws');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/likeUpdate', function (reviewUpdate) {
      updateLikeCount(JSON.parse(reviewUpdate.body));
    });
  });
  stompClient.subscribe('/topic/reviewUpdate', function (message) {
    const updateInfo = JSON.parse(message.body);
    if (updateInfo.reviewId === myReviewId) {
      showNotification(updateInfo.type, updateInfo.message);
    }
  });
}

function showNotification(type, message) {
  const notificationElement = document.createElement('div');
  notificationElement.className = `notification ${type}`;
  notificationElement.innerText = message;
  document.body.appendChild(notificationElement);
  setTimeout(() => notificationElement.remove(), 5000);
}


function updateLikeCount(reviewData) {
  const likeElement = document.querySelector(`.like-${reviewData.id}`);
  let isMany = " Like";
  if (reviewData.likesCount >= 2) {
    isMany = " Likes";
  }
  likeElement.textContent = reviewData.likesCount + isMany;
}

connect();


function findReview(shelfId, bookId) {
  fetch(`/review/find/${shelfId}/${bookId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    }
  })
      .then(response => {
        if (!response.ok) {
          return response.json().then(err => {
            throw new Error(err.message);  // 서버로부터 받은 메시지를 예외로 던짐
          });
        }
        return response.json();  // 정상 응답을 JSON으로 파싱
      })
      .then(data => {
        console.log('Success:', data);
        window.location.href = '/review/' + data.bookShelfId + '/' + data.bookId;

      })
      .catch(error => {
        console.error('Error:', error);
        updateFeedback(error.message, false);  // 에러 메시지를 updateFeedback 함수를 사용해 업데이트
      });
}


function updateFeedback(message, isSuccess) {
  var headerElement = document.getElementById('message-container');
  let feedbackElement = document.getElementById('rating-feedback');
  if (!feedbackElement) {
    feedbackElement = document.createElement('div');
    feedbackElement.id = 'rating-feedback';
    headerElement.appendChild(feedbackElement); // 수정된 부분
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


function createCommentElement(comment) {
  // 코멘트 컨테이너 생성
  const commentContainer = document.createElement('div');
  commentContainer.className = 'flex w-full justify-between border rounded-md p-3';
  commentContainer.id = `comment-update-${comment.reviewId}`;

  // 사용자 이미지 및 정보 포함하는 섹션
  const userInfoSection = document.createElement('div');
  userInfoSection.className = 'flex gap-3 items-center';

  const userAvatar = document.createElement('img');
  userAvatar.src = comment.member.profile;
  userAvatar.className = 'object-cover w-10 h-10 rounded-full border-2 border-emerald-400 shadow-emerald-400 flex-none';
  userInfoSection.appendChild(userAvatar);

  const userName = document.createElement('h3');
  userName.className = 'font-bold flex-none';
  let roleDisplayName = '';  // 역할을 한국어로 표시할 변수 초기화

  // 역할에 따라 한국어 이름 할당
  switch (comment.member.role) {
    case 'ROLE_USER':
      roleDisplayName = '유저';
      break;
    case 'ROLE_MANAGER':
      roleDisplayName = '매니저';
      break;
    case 'ROLE_ADMIN':
      roleDisplayName = '관리자';
      break;
    default:
      roleDisplayName = '알 수 없음';  // 정의되지 않은 역할 처리
  }

  // userName HTML 엘리먼트에 이름과 역할 표시
  userName.innerHTML = `${comment.member.name}<br><span class="text-sm text-gray-400 font-normal">${roleDisplayName}</span>`;
  userInfoSection.appendChild(userName);

  // 코멘트 텍스트 섹션
  const commentText = document.createElement('p');
  commentText.className = 'text-gray-600 mt-2 flex-1';
  commentText.textContent = comment.comment;
  userInfoSection.appendChild(commentText);

  // 버튼 섹션 추가
  const buttonSection = document.createElement('div');
  buttonSection.className = 'flex gap-2 mt-2';

  const deleteButton = document.createElement('button');
  deleteButton.textContent = '삭제';
  deleteButton.className = 'bg-red-500 h-[50px] w-[60px] text-white px-3 py-1 justify-self-center self-center rounded hover:bg-red-600';
  buttonSection.appendChild(deleteButton);

  const editButton = document.createElement('button');
  editButton.textContent = '수정';
  editButton.className = 'bg-blue-500 h-[50px] w-[60px] text-white px-3 py-1 justify-self-center self-center rounded hover:bg-blue-600';
  buttonSection.appendChild(editButton);

  console.log(comment.id);

  // 이벤트 핸들러 설정 (예시)
  deleteButton.onclick = () => deleteComment(comment.id, comment.reviewId);
  editButton.onclick = () => editComment(comment.id, "default", comment.reviewId);

  // 섹션을 코멘트 컨테이너에 추가
  commentContainer.appendChild(userInfoSection);
  commentContainer.appendChild(buttonSection);

  return commentContainer;
}

function updateCommentElement(reviewId,comment) {
  const commentContainer = document.getElementById(`comment-update-${reviewId}`);
  commentContainer.innerHTML = ``;

  // 사용자 이미지 및 정보 포함하는 섹션
  const userInfoSection = document.createElement('div');
  userInfoSection.className = 'flex gap-3 items-center';

  const userAvatar = document.createElement('img');
  userAvatar.src = comment.member.profile;
  userAvatar.className = 'object-cover w-10 h-10 rounded-full border-2 border-emerald-400 shadow-emerald-400 flex-none';
  userInfoSection.appendChild(userAvatar);

  const userName = document.createElement('h3');
  userName.className = 'font-bold flex-none';
  let roleDisplayName = '';  // 역할을 한국어로 표시할 변수 초기화

  // 역할에 따라 한국어 이름 할당
  switch (comment.member.role) {
    case 'ROLE_USER':
      roleDisplayName = '유저';
      break;
    case 'ROLE_MANAGER':
      roleDisplayName = '매니저';
      break;
    case 'ROLE_ADMIN':
      roleDisplayName = '관리자';
      break;
    default:
      roleDisplayName = '알 수 없음';  // 정의되지 않은 역할 처리
  }

  // userName HTML 엘리먼트에 이름과 역할 표시
  userName.innerHTML = `${comment.member.name}<br><span class="text-sm text-gray-400 font-normal">${roleDisplayName}</span>`;
  userInfoSection.appendChild(userName);

  // 코멘트 텍스트 섹션
  const commentText = document.createElement('p');
  commentText.className = 'text-gray-600 mt-2 flex-1';
  commentText.textContent = comment.comment;
  userInfoSection.appendChild(commentText);

  // 버튼 섹션 추가
  const buttonSection = document.createElement('div');
  buttonSection.className = 'flex gap-2 mt-2';

  const deleteButton = document.createElement('button');
  deleteButton.textContent = '삭제';
  deleteButton.className = 'bg-red-500 h-[50px] w-[60px] text-white px-3 py-1 justify-self-center self-center rounded hover:bg-red-600';
  buttonSection.appendChild(deleteButton);

  const editButton = document.createElement('button');
  editButton.textContent = '수정';
  editButton.className = 'bg-blue-500 h-[50px] w-[60px] text-white px-3 py-1 justify-self-center self-center rounded hover:bg-blue-600';
  buttonSection.appendChild(editButton);

  console.log(comment.id);

  // 이벤트 핸들러 설정 (예시)
  deleteButton.onclick = () => deleteComment(comment.id, comment.reviewId);
  editButton.onclick = () => editComment(comment.id, "default", comment.reviewId);

  // 섹션을 코멘트 컨테이너에 추가
  commentContainer.appendChild(userInfoSection);
  commentContainer.appendChild(buttonSection);

  return commentContainer;
}

function editComment(commentId, newText,reviewId) {
  // 서버에 PUT 요청을 보내 수정된 댓글 내용을 저장
  fetch(`/comment/update/${commentId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ text: newText })
  })
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to update comment');
        }
        return response.json();
      })
      .then(data => {

        updateCommentElement(reviewId,data.comment)

      })
      .catch(error => updateFeedback(error.message,false));
}

function deleteComment(commentId,reviewId) {
  fetch(`/comment/delete/${commentId}`, {
    method: 'DELETE', // 메서드 변경
    headers: {
      'Content-Type': 'application/json'
    }
  })
      .then(response => {
        if (response.ok) {
          document.getElementById(`comment-update-${reviewId}`).remove(); // 성공적으로 삭제되면 해당 댓글 DOM 제거
        } else {
          return response.json().then(data => {
            throw new Error(data.message || "Failed to delete comment"); // 서버로부터 오류 메시지가 제공된 경우 출력
          });
        }
      })
      .catch(error => {
        console.error(error); // 콘솔에 오류 메시지 출력
        updateFeedback(error.message, false); // 사용자에게 피드백 제공
      });
}