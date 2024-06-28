console.log("Smart Contact Manager");

document.addEventListener('DOMContentLoaded', function() {
            var inputs = document.querySelectorAll('input');
            inputs.forEach(function(input) {
                input.setAttribute('autocomplete', 'off');
            });
        });

const toggelSideBar=()=>{
        if($(".sidebar").is(":visible")){
            //true
            $('.sidebar').css("display","none");
            $(".content").css("margin-left","0%");
        }else{
            //false
            $('.sidebar').css("display","block");
            $(".content").css("margin-left","20%");
        }
    };


//Following function is used for check name contains only alphabets.
    function checkName(id) {
            var inputField = document.getElementById(id);
            var nameValue = inputField.value;
            // Example of further validation (e.g., regex pattern)
            var nameRegex = /^[A-Za-z ]+$/;
            if (!nameValue.match(nameRegex)) {
                inputField.classList.add('is-invalid');
                inputField.setCustomValidity('Name must contain only letters and spaces.');
                $("#"+id).val("");
            } else {
                inputField.classList.remove('is-invalid');
                inputField.setCustomValidity('');
            }
            // Update the field's validity
            inputField.reportValidity();
        }

//Following code for email validation check on onblur
function checkEmail(id) {
            var inputField = document.getElementById(id);
            var nameValue = inputField.value;
            // Example of further validation (e.g., regex pattern)
            var EMAIL_REGEX = "^(?=.{1,64}@.{4,64}$)(?=.{6,100}$)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!nameValue.match(EMAIL_REGEX)) {
                inputField.classList.add('is-invalid');
                inputField.setCustomValidity('Please Enter a valid email.');
            } else {
                inputField.classList.remove('is-invalid');
                inputField.setCustomValidity('');
            }
            // Update the field's validity
            inputField.reportValidity();
        }

//For password contains at least two special charecters
function checkPassword(id){
    var inputField = document.getElementById(id);
    var nameValue = inputField.value;
    var passwordRegex = "^(?=.*[^A-Za-z0-9].*[^A-Za-z0-9]).+$";
    if (!nameValue.match(passwordRegex)) {
          inputField.classList.add('is-invalid');
          inputField.setCustomValidity('Password contains atleast two special charecters.');
     } else {
          inputField.classList.remove('is-invalid');
          inputField.setCustomValidity('');
      }
      // Update the field's validity
      inputField.reportValidity();
}

//hide the HttpSession message after visible 4 seconds
document.addEventListener("DOMContentLoaded", function() {
        var messageContainer = document.getElementById("session_messageContainer");
        if (messageContainer) {
            setTimeout(function() {
                messageContainer.style.display = 'none'; // Hides the message
                // Optionally, you can remove it from the DOM
                // messageContainer.parentNode.removeChild(messageContainer);
            }, 2000); // 4000 milliseconds = 4 seconds
        }
    });

//Following code both functions used for upload user document(Profile Photo)
function chooseFileLocally() {
    document.getElementById('fileInput').click();
}

function previewFile() {
     const file = document.getElementById('fileInput').files[0]; //get the hidden input file (value)
     const reader = new FileReader();   //File Reader

     reader.onloadend = function() {
         document.getElementById('previewImg').src = reader.result; //assign selected image to view immediately
     }
     if (file) {
          reader.readAsDataURL(file);
     } else {
          document.getElementById('previewImg').src = "";
     }
}