// ===== FOOTER =====
const workingHours = document.getElementById('workingHours');
const tooltip = document.getElementById('tooltip');

function getBusinessStatus() {
    const now = new Date();
    const day = now.getDay();
    const hour = now.getHours();
    const minute = now.getMinutes();
    const openHour = 9;
    const closeHour = 18;
    const currentMinutes = hour * 60 + minute;

    const formatTime = (minutes) => {
        const h = Math.floor(minutes / 60);
        const m = minutes % 60;
        return `${h}ч ${m}м`;
    };

    if(day >= 1 && day <= 5){
        const openTime = openHour * 60;
        const closeTime = closeHour * 60;
        if(currentMinutes >= openTime && currentMinutes < closeTime){
            return `Открыто ✅ Закроется через ${formatTime(closeTime - currentMinutes)}`;
        } else if(currentMinutes < openTime){
            return `Закрыто ❌ Откроется через ${formatTime(openTime - currentMinutes)}`;
        } else {
            return `Закрыто ❌ Откроется через ${formatTime((24*60 - currentMinutes) + openHour*60)}`;
        }
    } else {
        const daysUntilMonday = (8 - day) % 7;
        const minutesLeft = daysUntilMonday*24*60 + (openHour*60 - currentMinutes % (24*60));
        return `Закрыто ❌ Откроется через ${formatTime(minutesLeft)}`;
    }
}

function showTooltip() {
    tooltip.style.display = 'block';
    tooltip.textContent = getBusinessStatus();
    const rect = workingHours.getBoundingClientRect();
    tooltip.style.top = rect.top + window.scrollY - tooltip.offsetHeight - 5 + 'px';
    tooltip.style.left = rect.left + window.scrollX + 'px';
}

function hideTooltip() {
    tooltip.style.display = 'none';
}

// ПК
workingHours.addEventListener('mouseenter', showTooltip);
workingHours.addEventListener('mouseleave', hideTooltip);

// Touch
workingHours.addEventListener('touchstart', showTooltip);
workingHours.addEventListener('touchend', hideTooltip);