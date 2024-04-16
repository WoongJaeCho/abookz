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
          console.log(book.author + "작가");
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

      } else {
        resultsDiv.textContent = 'No results found';
      }
    })
    .catch(error => {
      console.error('Error:', error);
    });

}

function createPaginationControls(totalPages, currentPage, query) {
  var paginationDiv = document.createElement('div');
  paginationDiv.id = 'pagingQueryDiv';
  paginationDiv.innerHTML = ''; // 기존 페이지네이션 컨트롤을 지우고 새로 시작

  for (let i = 0; i < totalPages; i++) {
    var pageButton = document.createElement('button');
    pageButton.className = "pagingQuery";
    pageButton.textContent = i + 1;
    pageButton.onclick = function () {
      fetchBooks(query, i, 10);
    };
    paginationDiv.appendChild(pageButton);
  }

  resultsDiv.appendChild(paginationDiv);
}

function createBookElement(book, resultsDiv) {
  var table = document.createElement('table');
  table.className = 'query_table';

  var coverRow = document.createElement('tr');
  coverRow.className = 'query_tr';
  var coverCell = document.createElement('td');
  coverCell.className = 'query_td';
  coverCell.setAttribute('rowspan', '5');
  coverCell.setAttribute('width', '300');
  var coverImage = document.createElement('img');
  coverImage.src = book.cover; // Ensure the `book.cover` URL is correct
  coverImage.className = "cover_img";
  coverImage.alt = 'Book Cover';
  var coverDetail = document.createElement('a');
  coverDetail.href = "/content/" + book.isbn13; // Ensure the link is correct
  coverDetail.appendChild(coverImage);
  coverCell.appendChild(coverDetail);
  coverRow.appendChild(coverCell);
  table.appendChild(coverRow);

  // Add title, author, and publication date rows
  appendTextRow(table, book.title);
  appendTextRow(table, book.author);
  appendTextRow(table, book.pubDate);

  resultsDiv.appendChild(table);
}

function appendTextRow(table, text) {
  var row = document.createElement('tr');
  var cell = document.createElement('td');
  cell.textContent = text; // Display the text in a cell
  row.appendChild(cell);
  table.appendChild(row);
}