export function copyToClipboard(elementId) {
    var element = document.getElementById(elementId);
    element.select();
    element.setSelectionRange(0, 99999);
    navigator.clipboard.writeText(element.value);
}