function searchBooks() {
    var query = document.getElementById('query').value;
    var resultsDiv = document.getElementById('searchResults');
    resultsDiv.innerHTML = '';

    if (query.trim().length < 2) {
        alert('2글자 이상 입력하세요.');
        return;
    }
    // 우리사이트에서 DB에 저장된것 sort
    if (!query.trim()) {
        fetch('/search')
            .then(response => response.json())
            .then(data => {
                if (data && data.length > 0) {
                    var table = document.createElement('table');
                    var headerRow = document.createElement('tr');
                    var titleHeader = document.createElement('th');
                    titleHeader.textContent = 'Title';
                    headerRow.appendChild(titleHeader);
                    table.appendChild(headerRow);

                    var authorHeaderRow = document.createElement('tr');
                    var authorHeader = document.createElement('th');
                    authorHeader.textContent = 'Author';
                    authorHeaderRow.appendChild(authorHeader);
                    table.appendChild(authorHeaderRow);

                    data.forEach(book => {

                        var titleRow = document.createElement('tr');
                        var titleCell = document.createElement('td');
                        titleCell.textContent = book.title;
                        titleRow.appendChild(titleCell);
                        table.appendChild(titleRow);

                        var authorRow = document.createElement('tr');
                        var authorCell = document.createElement('td');
                        authorCell.textContent = book.author;
                        authorRow.appendChild(authorCell);
                        table.appendChild(authorRow);
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
                    data.forEach(book => {

                        var table = document.createElement('table');
                        var coverRow = document.createElement('tr');
                        var coverHeader = document.createElement('th');

                        coverHeader.textContent = '';
                        coverRow.appendChild(coverHeader);
                        table.appendChild(coverRow);

                        var coverInfoRow = document.createElement('tr');
                        var coverCell = document.createElement('td');
                        var coverContent = document.createElement('a');
                        var coverImage = document.createElement('img');
                        // console.log("bookIsbn소"+book.ISBN13);/
                        console.log("bookIsbn1대"+book.isbn13);

                        coverContent.href = "/content/" +book.isbn13;
                        coverImage.src = book.cover;
                        coverContent.appendChild(coverImage);
                        coverCell.appendChild(coverContent);
                        coverInfoRow.appendChild(coverCell);
                        table.appendChild(coverInfoRow);

                        var titleRow = document.createElement('tr');

                        var titleHeader = document.createElement('th');
                        titleHeader.textContent = '제목';
                        titleRow.appendChild(titleHeader);

                        var titleCell = document.createElement('td');
                        titleCell.textContent = book.title;
                        titleRow.appendChild(titleCell);

                        table.appendChild(titleRow);

                        var authorRow = document.createElement('tr');
                        var authorHeader = document.createElement('th');
                        authorHeader.textContent = '작가';
                        authorRow.appendChild(authorHeader);

                        var authorCell = document.createElement('td');
                        authorCell.textContent = book.author;
                        authorRow.appendChild(authorCell);

                        table.appendChild(authorRow);


                        var pubDateRow = document.createElement('tr');
                        var pubDateHeader = document.createElement('th');
                        pubDateHeader.textContent = '출판날짜';
                        pubDateRow.appendChild(pubDateHeader);

                        var pubDateCell = document.createElement('td');
                        pubDateCell.textContent = book.pubDate;
                        pubDateRow.appendChild(pubDateCell);

                        table.appendChild(pubDateRow);

                        resultsDiv.appendChild(table);
                    });

                } else {
                    resultsDiv.textContent = 'No results found';
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
}