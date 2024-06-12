function LoginFormSubmit(event) {
    event.preventDefault(); // Prevent the form from submitting immediately

    Swal.fire({
        position: "center",
        icon: "success",
        title: "Please wait",
        showConfirmButton: false,
        timer: 1000
    }).then(() => {
        document.getElementById("loginForm").submit(); // Submit the form after the success message
    });
}
function AddCustomerFormSubmit(event) {
    event.preventDefault(); // Prevent the form from submitting immediately

    Swal.fire({
        position: "center",
        icon: "success",
        title: "Please wait",
        showConfirmButton: false,
        timer: 1000
    }).then(() => {
        document.getElementById("addCustomerForm").submit(); // Submit the form after the success message
    });
}
function deleteCustomer(customerId) {
    Swal.fire({
        position: "center",
        icon: "warning",
        title: "Are you sure?",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "No, cancel!"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = `/customer/delete/${customerId}`;
        }
    });
}
function deleteUser(userId) {
    Swal.fire({
        position: "center",
        icon: "warning",
        title: "Are you sure?",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "No, cancel!"
    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = `/user/delete/${userId}`;
        }
    });
}