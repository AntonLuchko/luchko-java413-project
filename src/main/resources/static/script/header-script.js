// ===== HEADER =====

// Бургер-меню + сдвиг контента
const burger = document.getElementById("burger");
const navbar = document.getElementById("navbar");
const pageContent = document.getElementById("pageContent");

burger.addEventListener("click", () => {
    const isOpen = navbar.classList.toggle("show");

    if (isOpen) {
        pageContent.classList.add("content-shift");
    } else {
        pageContent.classList.remove("content-shift");
    }
});

// Dropdown "О компании"
const dropdown = document.getElementById('about-dropdown');
const dropdownButton = dropdown.querySelector('button');

dropdownButton.addEventListener('click', (event) => {
    event.stopPropagation();
    dropdown.classList.toggle('show');
});

// Закрываем dropdown при клике вне него
document.addEventListener('click', (e) => {
    if (!dropdown.contains(e.target)) dropdown.classList.remove('show');
});