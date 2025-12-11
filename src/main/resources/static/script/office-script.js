



// ===== MAIN / MANAGER =====
start()
function start(){ const speechBubble = document.getElementById('speechBubble');
    const speechText = document.getElementById('speechText');
    const managerImg = document.querySelector('.people');
    const startBtn = document.getElementById("btn-start");

    const staticManagerSrc = "/img/stop.png";
    const gifManagerSrc = "/gif/speak.gif";

    const introText = "Привет! Я виртуальный помощник нашего онлайн офиса. Я могу ответить на часто задаваемые вопросы.";
    const faqItems = [
        "Как начать путешествие?",
        "Как связаться с оператором?",
        "Что делать в случае отмены путешествия?"
    ];

    function typeText(text, element, delay, callback) {
        element.textContent = "";
        speechBubble.style.opacity = 1;
        speechBubble.style.transform = "translateY(0)";
        managerImg.src = gifManagerSrc;

        let i = 0;
        const interval = setInterval(() => {
            element.textContent += text[i];
            i++;
            if (i >= text.length) {
                clearInterval(interval);
                managerImg.src = staticManagerSrc;
                if (callback) callback();
            }
        }, delay);
    }

    function showFAQ() {
        speechText.textContent = "";

        const ul = document.createElement("ul");
        ul.style.listStyle = "none";
        ul.style.padding = "0";
        ul.style.margin = "0";

        faqItems.forEach((item, index) => {
            const li = document.createElement("li");
            li.textContent = item;

            function highlightOn() {
                li.style.background = "#ffd700";
                li.style.color = "#0b3d91";
            }

            function highlightOff() {
                li.style.background = "";
                li.style.color = "";
            }

            // PC
            li.addEventListener("mouseenter", highlightOn);
            li.addEventListener("mouseleave", highlightOff);

            // Touch
            li.addEventListener("touchstart", highlightOn);
            li.addEventListener("touchend", highlightOff);

            li.addEventListener("click", () => {
                let answerText = "";
                if (index === 0) {
                    answerText = "Наверху панели есть кнопка Старт. Нажми на нее, чтобы выбрать материк и получить наш вариант отдыха!";
                    startBtn.style.background = "#ffd700";
                    startBtn.style.color = "#0b3d91";
                } else if (index === 1) {
                    answerText = "На нижней панели указана контактная информация. Будем рады ответить на все ваши вопросы!";
                } else if (index === 2) {
                    answerText = "Пожалуйста, уточните информацию у оператора.";
                }

                typeText(answerText, speechText, 40, () => {
                    setTimeout(() => {
                        startBtn.style.background = "";
                        startBtn.style.color = "";
                        showFAQ();
                    }, 2000);
                });
            });

            ul.appendChild(li);
        });

        speechText.appendChild(ul);
    }

    // Старт диалога
    typeText(introText, speechText, 50, showFAQ);}

