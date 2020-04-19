var selectedQuestions = new Set();
const prototypeForm = {

    getAvailableThemes: function () {
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/content/available/themes",
            dataType: 'json',
            timeout: 100000,
            success: function (themes) {
                console.log("SUCCESS: ", themes);
                prototypeForm.showAvailableThemes(themes);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            },
            done: function (d) {
                console.log("DONE");
            }
        });
    },

    showAvailableThemes: function (themes) {
        document.getElementById("pagination").innerHTML = '';
        let panel = document.getElementById("main-panel");
        panel.innerHTML = '';
        themes.forEach(x =>
            panel.innerHTML
                += `<a id="theme-${x.id}" name="theme" class="panel-block">
                        <span class="panel-icon">
                          <i class="far fa-folder"></i>
                        </span>
                        ${x.themeName}
                    </a>`
        );
        panel.innerHTML
            += `<a id="theme-0" name="theme-private" class="panel-block">
                        <span class="panel-icon">
                          <i class="fas fa-user-lock" style="color: brown"></i>
                        </span>
                        Your private questions
                    </a>`;

        let themesElems = document.getElementsByName("theme");
        themesElems.forEach(themeElem =>
            themeElem.addEventListener("click", (event) => {
                prototypeForm.getQuestions(themeElem.id.replace("theme-", ""))
            })
        );

        document
            .getElementById("theme-0")
            .addEventListener("click",
                e => prototypeForm.getQuestions(0, 0, true)
            );
    },

    getQuestions: function (themeId, pageNumber, isPrivate) {
        let requestParams = {
            themeId: themeId,
            pageNumber: pageNumber,
            isPrivate: isPrivate
        };

        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/content/questions",
            data: requestParams,
            dataType: 'json',
            timeout: 100000,
            success: function (response) {
                console.log("SUCCESS: ", response);
                prototypeForm.showQuestions(response, themeId);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            },
            done: function (d) {
                console.log("DONE");
            }
        });
    },

    showQuestions: function (response, themeId, isPrivate) {
        let questions = response.content;
        let panel = document.getElementById("main-panel");
        panel.innerHTML = '';
        questions.forEach(x =>
            panel.innerHTML
                += `<a id="question-${x.id}" name="question" class="panel-block">
                        <span class="panel-icon">
                          <i class="far fa-question-circle"></i>
                        </span>
                        ${x.question}
                    </a>`
        );

        let pageable = response.pageable;
        let totalPages = response.totalPages;
        let firstPage = 0;
        let currentPage = pageable.pageNumber;
        let prevPage = currentPage - 1;
        let nextPage = currentPage + 1;
        let lastPage = response.totalPages - 1;

        let pagination = document.getElementById("pagination");
        pagination.innerHTML = `
                <nav class="pagination is-right" role="navigation" aria-label="pagination">
                    <ul class="pagination-list">`;
        if (totalPages === 0 || totalPages === 1) {
            pagination.innerHTML += `<a class="pagination-link is-current">${totalPages}</a>`;
        } else if (currentPage === 1) {
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${firstPage}, ${isPrivate})">First Page</a>`;
            pagination.innerHTML += `<a class="pagination-link is-current">${currentPage + 1}</a>`;
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${nextPage}, ${isPrivate})">Next Page</a>`;
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${lastPage}, ${isPrivate})">Last Page</a>`;
        } else if (currentPage === lastPage - 1) {
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${firstPage}, ${isPrivate})">First Page</a>`;
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${prevPage}, ${isPrivate})">Previous Page</a>`;
            pagination.innerHTML += `<a class="pagination-link is-current">${currentPage + 1}</a>`;
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${lastPage}, ${isPrivate})">Last Page</a>`;
        } else if (currentPage === 0) {
            pagination.innerHTML += `<a class="pagination-link is-current">${currentPage + 1}</a>`;
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${nextPage}, ${isPrivate})">Next Page</a>`;
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${lastPage}, ${isPrivate})">Last Page</a>`;
        } else if (currentPage === lastPage) {
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${firstPage}, ${isPrivate})">First Page</a>`;
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${prevPage}, ${isPrivate})">Previous Page</a>`;
            pagination.innerHTML += `<a class="pagination-link is-current">${currentPage + 1}</a>`;
        } else {
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${firstPage}, ${isPrivate})">First Page</a>`;
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${prevPage}, ${isPrivate})">Previous Page</a>`;
            pagination.innerHTML += `<a class="pagination-link is-current">${currentPage + 1}</a>`;
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${nextPage}, ${isPrivate})">Next Page</a>`;
            pagination.innerHTML += `<a class="pagination-link" onclick="prototypeForm.getQuestions(${themeId}, ${lastPage}, ${isPrivate})">Last Page</a>`;
        }

        pagination.innerHTML += `</ul></nav>`;

        panel.innerHTML += `<a id="back-element"
                               name="back-element" 
                               class="panel-block"
                               onclick="prototypeForm.getAvailableThemes()"
                               >
                                <span class="panel-icon">
                                  <i class="fas fa-chevron-circle-left" style="color: red"></i>
                                </span>
                                Go back to themes
                            </a>`;

        let questionElems = document.getElementsByName("question");
        questionElems.forEach(questionElem => {
                questionElem.addEventListener("click", (event) => {
                    prototypeForm.addQuestion(questionElem);
                })
            }
        );
    },

    addQuestion: function (questionElem) {
        let questionId = questionElem.id.replace("question-", "");
        let questionVal = questionElem.innerText;
        if (!selectedQuestions.has(questionId)) {
            selectedQuestions.add(questionId);

            let selectedElems = document.getElementById("selected-questions");
            selectedElems.innerHTML +=
                `<div id="selected-question-${questionId}" class="notification is-info is-light is-small">
                    <a id="delete-${questionId}" onclick="prototypeForm.deleteQuestion(${questionId})" class="delete is-large"></a>
                    ${questionVal}
                 </div>`;
        }
    },

    deleteQuestion: function (questionId) {
        let toDelete = document.getElementById("selected-question-" + questionId);
        selectedQuestions.delete(questionId);
        document.getElementById("selected-questions").removeChild(toDelete);
    },

    submitListener: function () {
        document.getElementById("selectedQuestions").value = Array.from(selectedQuestions);

        return true;
    }
};

prototypeForm.getAvailableThemes();