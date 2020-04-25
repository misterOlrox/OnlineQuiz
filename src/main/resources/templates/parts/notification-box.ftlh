<div id="notification-box" style="position: fixed;
         right: 1%;
         bottom: 2%;
         max-height: 60%;
         width: 20%;" hidden>
    <div class="card" style="background-color: blanchedalmond;margin-top: 1%;margin-bottom: 1%">
        <header class="card-header">
            <p class="card-header-title">
                Invitations
            </p>
            <a class="card-header-icon" aria-label="more options" onclick="notifications.close()">
              <span class="icon">
                <i class="fa fa-times" aria-hidden="true" style="color: #eb005f"></i>
              </span>
            </a>
        </header>
        <div class="card-content">
            <div class="content">
                You have new invitations
            </div>
        </div>
        <footer class="card-footer" style="border-color: transparent">
            <a href="/user/invitations" class="card-footer-item" style="border-color: lightslategrey; color: green;">Show me</a>
            <a onclick="notifications.close()" class="card-footer-item" style="border-color: lightslategrey; color: #eb005f">Close</a>
        </footer>
    </div>
</div>