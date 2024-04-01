function searchBooks() {
    var query = document.getElementById('query').value;
    var resultsDiv = document.getElementById('searchResults');
    resultsDiv.innerHTML = '';

    if (query.trim().length < 2) {
        alert('2글자 이상 입력하세요.');
        return;
    }

    if (!query.trim()) {
        // 검색어가 비어 있다면 기본 책 정보를 받아오도록 서버에 요청
        fetch('/search')
            .then(response => response.json())
            .then(data => {
                if (data && data.length > 0) {
                    var table = document.createElement('table');

                    // 테이블 헤더 생성
                    var headerRow = document.createElement('tr');
                    var titleHeader = document.createElement('th');
                    titleHeader.textContent = 'Title';
                    headerRow.appendChild(titleHeader);
                    var authorHeader = document.createElement('th');
                    authorHeader.textContent = 'Author';
                    headerRow.appendChild(authorHeader);
                    table.appendChild(headerRow);

                    // 테이블 데이터 추가
                    data.forEach(book => {
                        var row = document.createElement('tr');
                        var titleCell = document.createElement('td');
                        titleCell.textContent = book.title;
                        row.appendChild(titleCell);
                        var authorCell = document.createElement('td');
                        authorCell.textContent = book.author;
                        row.appendChild(authorCell);
                        table.appendChild(row);
                    });

                    resultsDiv.appendChild(table);
                } else {
                    resultsDiv.textContent = 'No books found';
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    } else {
        // 검색어가 비어 있지 않다면 검색어를 포함하여 서버에 요청
        fetch('/search?query=' + query, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ query: query })
        })
            .then(response => response.json())
            .then(data => {
                if (data && data.length > 0) {
                    var table = document.createElement('table');

                    // 테이블 헤더 생성
                    var headerRow = document.createElement('tr');
                    var titleHeader = document.createElement('th');
                    titleHeader.textContent = 'Title';
                    headerRow.appendChild(titleHeader);
                    var authorHeader = document.createElement('th');
                    authorHeader.textContent = 'Author';
                    headerRow.appendChild(authorHeader);
                    table.appendChild(headerRow);

                    // 테이블 데이터 추가
                    data.forEach(book => {
                        var row = document.createElement('tr');
                        var titleCell = document.createElement('td');
                        titleCell.textContent = book.title;
                        row.appendChild(titleCell);
                        var authorCell = document.createElement('td');
                        authorCell.textContent = book.author;
                        row.appendChild(authorCell);
                        table.appendChild(row);
                    });

                    resultsDiv.appendChild(table);
                } else {
                    resultsDiv.textContent = 'No results found';
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}