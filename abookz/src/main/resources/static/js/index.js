var resultsDiv = document.getElementById('searchResults');

function searchBooks() {
  var query = document.getElementById('query').value;
  resultsDiv.innerHTML = '';
  if (!query.trim()) {
    alert('검색어를 입력해주세요');
    return;

  }
  fetchBooks(query, 0, 10)
}

function fetchBooks(query, page, size) {
  resultsDiv.innerHTML = '';
  const url = '/search';
  const data = {
    query: query,
    page: page,
    size: size
  };
  fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
      console.log(data);
      if (data.content && data.content.length > 0) {
        data.content.forEach(book => {
          createBookElement(book, resultsDiv);
        });
        createPaginationControls(data.totalPages, data.number, query);
        let shelfContainer = document.getElementById('shelf_container');
        if (shelfContainer) {
          shelfContainer.style.display = 'none';
        }
        let mainContainer = document.getElementById('main');
        if (mainContainer) {
          mainContainer.style.display = 'none';
        }
        let bookContainer = document.getElementById("book_container");
        if (bookContainer) {
          bookContainer.style.display = 'none';
        }
        let bookWarpContainer = document.getElementById('bookWrap');
        if(bookWarpContainer){
          bookWarpContainer.style.display = 'none';
        }
        let controlsContainer =document.getElementById('controls_container');
        if(controlsContainer){
          controlsContainer.style.display = 'none';
        }
        let canvasDiv= document.getElementById('canvas');
        if(canvasDiv){
          canvasDiv.style.display='none';
        }
        let listWrapDiv = document.getElementById('listWrap');
        if(listWrapDiv){
          listWrapDiv.style.display= 'none';
        }
        let boardContainer = document.getElementById('boardContainer');
          if(boardContainer){
            boardContainer.style.display='none';
          }
      } else {
        resultsDiv.textContent = '검색 결과가 없습니다';
      }
    })
    .catch(error => {
      console.error('Error:', error);
    });

}

function createPaginationControls(totalPages, currentPage, query) {
  var paginationDiv = document.createElement('div');
  paginationDiv.id = 'pagingQueryDiv';
  paginationDiv.innerHTML = '';

  for (let i = 0; i < totalPages; i++) {
    var pageButton = document.createElement('button');
    pageButton.className = "pagingQuery";
    pageButton.textContent = i + 1;
    // 클로저 문제를 해결하기 위해 각 버튼에 대한 클로저 생성
    (function (page) {
      pageButton.onclick = function () {
        fetchBooks(query, page, 10);
      };
    })(i);
    paginationDiv.appendChild(pageButton);
  }

  resultsDiv.appendChild(paginationDiv);
}

function createBookElement(book, resultsDiv) {

  var divElement = document.createElement('div');
  divElement.className = 'queryBook card lg:card-side bg-base-100 shadow-xl flex justify-center mt-2 mb-2 w-[900px] h-[300px]';


  var figure = document.createElement('figure');
  figure.className = 'image-full';

  var alink = document.createElement('a');
  alink.href = "/content/" + book.isbn13;

  var coverImage = document.createElement('img');
  coverImage.src = book.cover; // Ensure the `book.cover` URL is correct
  coverImage.className = "image-full";
  coverImage.alt = 'Book Cover';


  var divBody = document.createElement('div');
  divBody.className = "card-body w-[700px]";

  var pTitle = document.createElement('p');
  pTitle.className = "text-lg";
  var spanTitle = document.createElement("span");
  var titleText = document.createTextNode(book.title.length > 30 ? book.title.substring(0, 30) + '...' : book.title);


  var pAuthor = document.createElement('p');
  pAuthor.className = "text-sm";
  var spanAuthor = document.createElement("span");
  var authorText = document.createTextNode(book.author.length > 30 ? book.author.substring(0, 30) + '...' : book.author);

  // Create the paragraph element for the book description
  var pDescription = document.createElement('p');
  pDescription.className = "text-sm";
  var spanDescription = document.createElement("span");
  spanDescription.innerHTML = book.description.replace(/\n/g, '<br/>');
  var divButton = document.createElement('div');
  divButton.className = 'card-actions justify-end';
  var buttonMyShelf = document.createElement('button');
  buttonMyShelf.className = 'btn btn-primary';
  buttonMyShelf.textContent = '서재 담기';
  buttonMyShelf.addEventListener('click', function () {
    var myShelfBook = book.isbn13
    console.log(myShelfBook + " myShelfBook");
    wantToRead(myShelfBook)
  })
  resultsDiv.appendChild(divElement);
  divElement.appendChild(figure);
  figure.appendChild(alink);
  alink.appendChild(coverImage);
  divElement.appendChild(divBody);
  divBody.appendChild(pTitle);
  pTitle.appendChild(spanTitle);
  spanTitle.appendChild(titleText);
  divBody.appendChild(pAuthor);
  pAuthor.appendChild(spanAuthor);
  spanAuthor.appendChild(authorText);
  divBody.appendChild(pDescription);
  pDescription.appendChild(spanDescription);
  divBody.appendChild(divButton);
  divButton.appendChild(buttonMyShelf);
}



function wantToRead(isbn) {
  var isbn13 = isbn;
  fetch('/want?book=' + isbn13, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        isbn13: isbn13
      })
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
      updateFeedback("로그인이 필요합니다", false);

    });

}

// 피드백 메시지 업데이트
function updateFeedback(message, isSuccess ) {
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
