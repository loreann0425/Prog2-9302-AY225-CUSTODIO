// Valid credentials (you can add more users here)
const validUsers = {
    'seah': '89167236'
};

// Attendance records storage
let attendanceRecords = [];
let currentUser = null;
let currentTimestamp = null;

// Create beep sound using Web Audio API
function createBeepSound() {
    const audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const oscillator = audioContext.createOscillator();
    const gainNode = audioContext.createGain();
    
    oscillator.connect(gainNode);
    gainNode.connect(audioContext.destination);
    
    oscillator.frequency.value = 800;
    oscillator.type = 'sine';
    
    gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
    gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5);
    
    oscillator.start(audioContext.currentTime);
    oscillator.stop(audioContext.currentTime + 0.5);
}

// Format timestamp
function formatTimestamp(date) {
    const months = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];
    const month = months[date.getMonth()];
    const day = String(date.getDate()).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    
    return `${month}/${day}/${year} ${hours}:${minutes}:${seconds}`;
}

// Toggle password visibility
document.getElementById('togglePassword').addEventListener('click', function() {
    const passwordInput = document.getElementById('password');
    const type = passwordInput.type === 'password' ? 'text' : 'password';
    passwordInput.type = type;
    this.textContent = type === 'password' ? '👁️' : '🙈';
});

// Handle form submission
document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    const errorMessage = document.getElementById('errorMessage');
    const loginBtn = document.getElementById('loginBtn');
    const btnText = document.getElementById('btnText');
    
    // Show loading state
    loginBtn.disabled = true;
    btnText.innerHTML = '<span class="loading"></span>';
    
    // Simulate processing delay
    setTimeout(() => {
        // Validate credentials
        if (validUsers[username] && validUsers[username] === password) {
            // Successful login
            errorMessage.style.display = 'none';
            
            // Capture timestamp
            const loginDate = new Date();
            currentUser = username;
            currentTimestamp = formatTimestamp(loginDate);
            
            // Store attendance record
            attendanceRecords.push({
                username: username,
                timestamp: currentTimestamp,
                fullTimestamp: loginDate.toString()
            });
            
            // Show success screen
            document.getElementById('loginScreen').style.display = 'none';
            document.getElementById('successScreen').style.display = 'block';
            document.getElementById('displayUsername').textContent = username;
            document.getElementById('timestampDisplay').textContent = currentTimestamp;
        } else {
            // Failed login - play beep and show error
            createBeepSound();
            errorMessage.style.display = 'block';
            document.getElementById('password').value = '';
            document.getElementById('password').focus();
        }
        
        // Reset button state
        loginBtn.disabled = false;
        btnText.textContent = 'Log In';
    }, 800);
});

// Download attendance summary
document.getElementById('downloadBtn').addEventListener('click', function() {
    let summaryContent = '═══════════════════════════════════════════════\n';
    summaryContent += '        ATTENDANCE SUMMARY REPORT\n';
    summaryContent += '═══════════════════════════════════════════════\n\n';
    summaryContent += `Generated: ${formatTimestamp(new Date())}\n\n`;
    summaryContent += '───────────────────────────────────────────────\n';
    summaryContent += 'CURRENT SESSION\n';
    summaryContent += '───────────────────────────────────────────────\n\n';
    summaryContent += `Username: ${currentUser}\n`;
    summaryContent += `Login Time: ${currentTimestamp}\n`;
    summaryContent += `Status: PRESENT\n\n`;
    
    if (attendanceRecords.length > 1) {
        summaryContent += '───────────────────────────────────────────────\n';
        summaryContent += 'PREVIOUS SESSIONS\n';
        summaryContent += '───────────────────────────────────────────────\n\n';
        
        attendanceRecords.slice(0, -1).forEach((record, index) => {
            summaryContent += `Session ${index + 1}:\n`;
            summaryContent += `  Username: ${record.username}\n`;
            summaryContent += `  Timestamp: ${record.timestamp}\n\n`;
        });
    }
    
    summaryContent += '═══════════════════════════════════════════════\n';
    summaryContent += `Total Records: ${attendanceRecords.length}\n`;
    summaryContent += '═══════════════════════════════════════════════\n';
    
    // Create and download file
    const blob = new Blob([summaryContent], { type: 'text/plain' });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(blob);
    link.download = `attendance_summary_${currentUser}_${Date.now()}.txt`;
    link.click();
    
    // Cleanup
    window.URL.revokeObjectURL(link.href);
});

// Logout functionality
document.getElementById('logoutBtn').addEventListener('click', function() {
    document.getElementById('successScreen').style.display = 'none';
    document.getElementById('loginScreen').style.display = 'block';
    document.getElementById('username').value = '';
    document.getElementById('password').value = '';
    document.getElementById('errorMessage').style.display = 'none';
    currentUser = null;
    currentTimestamp = null;
});

// Focus username field on load
document.getElementById('username').focus();