function logout() {
    var a = document.createElement('a')
    a.href = '/logout'
    a.target = '_self'
    document.body.appendChild(a)
    a.click()
}