document.addEventListener('DOMContentLoaded', function() {
  var initialSortType = document.querySelector('input[name="최신순"]');
  loadReviews("최신순",initialSortType);
});

// 전체 선택 토글 스크립트
function toggleCheckboxes(source) {
  const checkboxes = document.getElementsByName('selectedReviews');
  for (let checkbox of checkboxes) {
    checkbox.checked = source.checked;
  }
}

// 선택된 리뷰 삭제 및 비동기 처리
function deleteSelectedReviews() {
  if (confirm('선택한 리뷰를 삭제하시겠습니까?')) {
    const selectedReviews = Array.from(document.getElementsByName('selectedReviews'))
        .filter(checkbox => checkbox.checked)
        .map(checkbox => checkbox.value);

    console.log(selectedReviews);
    fetch('/review/delete', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ reviews: selectedReviews })
    })
        .then(response => {
          if (!response.ok) {
            return response.text().then(text => { throw new Error(text) }); // 에러 메시지를 텍스트로 변환 후 예외를 던집니다.
          }
          return response.json();
        })
        .then(data => {
          console.log(data);
          loadReviews(document.querySelector('input[name="sort"]:checked').value);
        })
        .catch(error => {
          console.error(error);
          alert('리뷰 삭제 중 문제가 발생했습니다: ' + error.message); // 오류 메시지를 보다 상세하게 표시합니다.
        });
  }
}

function loadReviews(sortType, element) {
  console.log("리뷰 데이터 로드: " + sortType);
  const url = `/review/myReviews?sort=${encodeURIComponent(sortType)}`;

  fetch(url, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  })
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => {
        const tabPanelId = `tab-${sortType}`;
        updateTable(data.reviews, tabPanelId);
      })
      .catch(error => {
        console.error('Error loading the reviews:', error);
        alert('리뷰를 로드하는 중 문제가 발생했습니다.');
      });
}

function updateTable(reviews, tabPanelId) {
  const tabPanel = document.getElementById(tabPanelId);
  tabPanel.innerHTML = ''; // 선택된 탭 내용을 비우기
  const wrap = document.createElement('div')
  wrap.className = "overflow-x-auto mt-4";
  tabPanel.appendChild(wrap);
  const table = document.createElement('table');
  table.className = 'table w-full text-gray-700';
  table.innerHTML = `
    <thead>
      <tr class="text-xs font-semibold uppercase text-gray-600 bg-gray-100">
        <th><label><input id="all-check" type="checkbox" class="checkbox checkbox-primary" onchange="toggleCheckboxes(this)"/></label></th>
        <th>No.</th>
        <th>사진</th>
        <th>제목</th>
        <th>내용</th>
        <th>날짜</th>
        <th>스포일러</th>
        <th>좋아요</th>
        <th>수정하기</th>
      </tr>
    </thead>
    <tbody>
    </tbody>
  `;

  const tbody = table.querySelector('tbody');
  reviews.forEach((review, index) => {
    const row = document.createElement('tr');
    row.className = "bg-white border-b hover:bg-gray-50";
    row.innerHTML = `
      <th><label><input type="checkbox" name="selectedReviews" class="checkbox checkbox-secondary" value="${review.id}"/></label></th>
      <td>${index + 1}</td>
      <td><img src="${review.bookShelfDTO.bookDTO.cover}" class="h-20 w-20 object-cover mask mask-squircle shadow" alt="Book Image"/></td>
      <td>${review.bookShelfDTO.bookDTO.title}</td>
      <td>${review.content}</td>
      <td>${new Date(review.createdDate).toISOString().slice(0, 10)}</td>
      <td>${review.isSpoilerActive ? 'Yes' : 'No'}</td>
      <td>${review.likesCount}</td>
      <td><a href="/review/${review.bookShelfDTO.id}/${review.bookShelfDTO.bookDTO.id}" class="btn btn-outline btn-primary btn-sm">수정</a></td>
      
    `;
    tbody.appendChild(row);
  });

  wrap.appendChild(table); // 테이블을 탭 패널에 추가
  var newButton = document.createElement("button");
  newButton.setAttribute("type", "submit");
  newButton.setAttribute("className", "btn btn-error mt-4");
  newButton.setAttribute("onClick", "deleteSelectedReviews()");
  newButton.textContent = "삭제하기";

// tabPanel에 새로운 버튼 추가
  tabPanel.appendChild(newButton);
}
