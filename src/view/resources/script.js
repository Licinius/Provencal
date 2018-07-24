(function getSelectionText() {
                let text = '';
                if (window.getSelection) {
                    text = window.getSelection().toString();
                } else if (document.selection && document.selection.type != 'Control') {
                    text = document.selection.createRange().text;
                }
                if (window.getSelection) {
                  if (window.getSelection().empty) {  // Chrome
                    window.getSelection().empty();
                  } else if (window.getSelection().removeAllRanges) {  // Firefox
                    window.getSelection().removeAllRanges();
                  }
                } else if (document.selection) {  // IE
                  document.selection.empty();
                }
                return text;
})()