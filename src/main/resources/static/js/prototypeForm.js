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
        let themesElems = document.getElementsByName("theme");
        themesElems.forEach(themeElem =>
            themeElem.addEventListener("click", (event) => {
                prototypeForm.getQuestions(themeElem.id.replace("theme-", ""))
            })
        )
    },

    getQuestions: function (themeId) {
        let requestParams = {
            themeId: themeId
        };

        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/content/questions",
            data: requestParams,
            dataType: 'json',
            timeout: 100000,
            success: function (questions) {
                console.log("SUCCESS: ", questions);
                prototypeForm.showQuestions(questions);
            },
            error: function (e) {
                console.log("ERROR: ", e);
            },
            done: function (d) {
                console.log("DONE");
            }
        });
    },

    showQuestions: function (questions) {
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
        let questionElems = document.getElementsByName("question");
        // questionElems.forEach(questionElem =>
        //     questionElem.addEventListener("click", (event) => {
        //         prototypeForm.getQuestions(questionElem.id.replace("question-", ""))
        //     })
        // );

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
    },
};

prototypeForm.getAvailableThemes();