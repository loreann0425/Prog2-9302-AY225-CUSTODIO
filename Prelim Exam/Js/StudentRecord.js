// Programmer: Lore Ann Custodio - 23-1068-533
// Student Record System - JavaScript Implementation

let students = [];
let selectedIndex = -1;

// Load data when page loads
window.addEventListener('DOMContentLoaded', async () => {
    console.log('Page loaded - initializing...');
    await loadCSVData();
    setupEventListeners();
});

function setupEventListeners() {
    // Search functionality
    document.getElementById('searchBox').addEventListener('input', searchTable);
    
    // Table row selection
    document.getElementById('tableBody').addEventListener('click', (e) => {
        const row = e.target.closest('tr');
        if (row) {
            selectRow(row);
        }
    });
    
    console.log('Event listeners setup complete');
}

async function loadCSVData() {
    // Try multiple relative paths so file can be found whether served from `Js` or project root
    const candidates = ['Mock.csv', 'MOCK_DATA.csv', '../Java/Mock.csv', '../Java/MOCK_DATA.csv', '../Mock.csv'];
    let csvText = null;

    for (const path of candidates) {
        try {
            const res = await fetch(path);
            if (res.ok) {
                csvText = await res.text();
                console.log('Loaded CSV from', path);
                break;
            } else {
                console.warn('CSV fetch failed', path, res.status);
            }
        } catch (err) {
            console.warn('CSV fetch error for', path, err.message);
        }
    }

    if (!csvText) {
        console.error('Could not load Mock.csv from any candidate paths');
        alert('Could not find Mock.csv. Put Mock.csv in the same folder as index.html or run a server that exposes the file.');
        renderTable(); // still render empty table
        return;
    }

    // Split lines robustly (handles CRLF)
    const lines = csvText.split(/\r?\n/);

    for (let i = 0; i < lines.length; i++) {
        const raw = lines[i].trim();
        if (!raw) continue;
        if (i === 0 && /student.*id/i.test(raw)) continue; // skip header

        const values = parseCSVLine(raw);
        if (values.length >= 8) {
            const student = {
                studentID: values[0].trim(),
                firstName: values[1].trim(),
                lastName: values[2].trim(),
                labWork1: parseInt(values[3].trim()) || 0,
                labWork2: parseInt(values[4].trim()) || 0,
                labWork3: parseInt(values[5].trim()) || 0,
                prelimExam: parseInt(values[6].trim()) || 0,
                attendance: parseInt(values[7].trim()) || 0
            };
            students.push(student);
        }
    }

    console.log('Loaded ' + students.length + ' students from Mock.csv (candidates tried)');
    renderTable();
}

// Basic CSV line parser that handles quoted fields with commas
function parseCSVLine(line) {
    const result = [];
    let cur = '';
    let inQuotes = false;

    for (let i = 0; i < line.length; i++) {
        const ch = line[i];
        if (ch === '"') {
            // toggle inQuotes unless it's an escaped quote
            if (inQuotes && line[i + 1] === '"') {
                cur += '"';
                i++; // skip escaped quote
            } else {
                inQuotes = !inQuotes;
            }
        } else if (ch === ',' && !inQuotes) {
            result.push(cur);
            cur = '';
        } else {
            cur += ch;
        }
    }
    result.push(cur);
    return result;
}

function renderTable(data = students) {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '';
    
    data.forEach((student, index) => {
        const row = tbody.insertRow();
        row.dataset.index = index;
        
        row.innerHTML = `
            <td>${student.studentID}</td>
            <td>${student.firstName}</td>
            <td>${student.lastName}</td>
            <td>${student.labWork1}</td>
            <td>${student.labWork2}</td>
            <td>${student.labWork3}</td>
            <td>${student.prelimExam}</td>
            <td>${student.attendance}</td>
        `;
    });
    
    updateStats(data.length);
}

function selectRow(row) {
    // Remove previous selection
    const previousSelected = document.querySelector('tr.selected');
    if (previousSelected) {
        previousSelected.classList.remove('selected');
    }
    
    // Add selection
    row.classList.add('selected');
    selectedIndex = parseInt(row.dataset.index);
    
    console.log('Selected row:', selectedIndex);
    
    // Populate fields
    populateFields();
}

function populateFields() {
    if (selectedIndex === -1 || selectedIndex >= students.length) return;
    
    const student = students[selectedIndex];
    
    document.getElementById('studentID').value = student.studentID;
    document.getElementById('firstName').value = student.firstName;
    document.getElementById('lastName').value = student.lastName;
    document.getElementById('labWork1').value = student.labWork1;
    document.getElementById('labWork2').value = student.labWork2;
    document.getElementById('labWork3').value = student.labWork3;
    document.getElementById('prelimExam').value = student.prelimExam;
    document.getElementById('attendance').value = student.attendance;
}

function clearFields() {
    console.log('CLEAR button clicked');
    
    document.getElementById('studentID').value = '';
    document.getElementById('firstName').value = '';
    document.getElementById('lastName').value = '';
    document.getElementById('labWork1').value = '';
    document.getElementById('labWork2').value = '';
    document.getElementById('labWork3').value = '';
    document.getElementById('prelimExam').value = '';
    document.getElementById('attendance').value = '';
    
    const selected = document.querySelector('tr.selected');
    if (selected) {
        selected.classList.remove('selected');
    }
    selectedIndex = -1;
    
    console.log('Fields cleared');
}

// CREATE - Add new student record
function createRecord() {
    console.log('CREATE button clicked');
    
    const studentID = document.getElementById('studentID').value.trim();
    const firstName = document.getElementById('firstName').value.trim();
    const lastName = document.getElementById('lastName').value.trim();
    const labWork1 = parseInt(document.getElementById('labWork1').value) || 0;
    const labWork2 = parseInt(document.getElementById('labWork2').value) || 0;
    const labWork3 = parseInt(document.getElementById('labWork3').value) || 0;
    const prelimExam = parseInt(document.getElementById('prelimExam').value) || 0;
    const attendance = Number(document.getElementById('attendance').value) || 0;
    
    // Validation
    if (!studentID) {
        alert('❌ Student ID is required!');
        return;
    }
    
    // Check for duplicate
    const duplicate = students.find(s => s.studentID === studentID);
    if (duplicate) {
        alert('❌ Student ID already exists!\n\nPlease use a different Student ID.');
        return;
    }
    
    // Validate grades (attendance validated separately - no 0-100 limit)
    if (!validateGrades(labWork1, labWork2, labWork3, prelimExam) || !validateAttendance(attendance)) {
        return;
    }
    
    // Create new student
    const newStudent = {
        studentID,
        firstName,
        lastName,
        labWork1,
        labWork2,
        labWork3,
        prelimExam,
        attendance
    };
    
    students.push(newStudent);
    renderTable();
    clearFields();
    
    console.log('Student created:', newStudent);
    
    alert('✅ Student record created successfully!\n\n' +
          'Student ID: ' + studentID + '\n' +
          'Name: ' + firstName + ' ' + lastName);
}

// READ - Display selected record
function readRecord() {
    console.log('READ button clicked');
    
    if (selectedIndex === -1) {
        alert('❌ Please select a record from the table to view!');
        return;
    }
    
    const student = students[selectedIndex];
    
    const info = `
═══════════════════════════════
       STUDENT DETAILS
═══════════════════════════════

Student ID: ${student.studentID}
Name: ${student.firstName} ${student.lastName}

GRADES:
  Lab Work 1: ${student.labWork1}
  Lab Work 2: ${student.labWork2}
  Lab Work 3: ${student.labWork3}
  Prelim Exam: ${student.prelimExam}
  Attendance: ${student.attendance}
    `;
    
    console.log('Reading student:', student);
    alert(info);
}

// UPDATE - Modify existing record
function updateRecord() {
    console.log('UPDATE button clicked');
    
    if (selectedIndex === -1) {
        alert('❌ Please select a record from the table to update!');
        return;
    }
    
    const studentID = document.getElementById('studentID').value.trim();
    const firstName = document.getElementById('firstName').value.trim();
    const lastName = document.getElementById('lastName').value.trim();
    const labWork1 = parseInt(document.getElementById('labWork1').value) || 0;
    const labWork2 = parseInt(document.getElementById('labWork2').value) || 0;
    const labWork3 = parseInt(document.getElementById('labWork3').value) || 0;
    const prelimExam = parseInt(document.getElementById('prelimExam').value) || 0;
    const attendance = Number(document.getElementById('attendance').value) || 0;
    
    // Validation
    if (!studentID) {
        alert('❌ Student ID cannot be empty!');
        return;
    }
    
    // Validate grades (attendance validated separately - no 0-100 limit)
    if (!validateGrades(labWork1, labWork2, labWork3, prelimExam) || !validateAttendance(attendance)) {
        return;
    }
    
    // Check for duplicate (excluding current record)
    const duplicate = students.find((s, index) => 
        s.studentID === studentID && index !== selectedIndex
    );
    if (duplicate) {
        alert('❌ Student ID already exists!\n\nPlease use a different Student ID.');
        return;
    }
    
    // Update student
    students[selectedIndex] = {
        studentID,
        firstName,
        lastName,
        labWork1,
        labWork2,
        labWork3,
        prelimExam,
        attendance
    };
    
    renderTable();
    clearFields();
    
    console.log('Student updated:', students[selectedIndex]);
    
    alert('✅ Student record updated successfully!\n\n' +
          'Updated: ' + firstName + ' ' + lastName);
}

// DELETE - Remove record
function deleteRecord() {
    console.log('DELETE button clicked');
    
    if (selectedIndex === -1) {
        alert('❌ Please select a record from the table to delete!');
        return;
    }
    
    const student = students[selectedIndex];
    
    const confirmed = confirm(
        '⚠️ Are you sure you want to delete this record?\n\n' +
        'Student: ' + student.firstName + ' ' + student.lastName + '\n' +
        'ID: ' + student.studentID + '\n\n' +
        'This action cannot be undone!'
    );
    
    if (confirmed) {
        console.log('Deleting student:', student);
        
        students.splice(selectedIndex, 1);
        renderTable();
        clearFields();
        
        alert('✅ Student record deleted successfully!');
    }
}

function searchTable() {
    const searchTerm = document.getElementById('searchBox').value.toLowerCase().trim();
    
    if (!searchTerm) {
        renderTable();
        return;
    }
    
    const filtered = students.filter(student => 
        student.studentID.toLowerCase().includes(searchTerm) ||
        student.firstName.toLowerCase().includes(searchTerm) ||
        student.lastName.toLowerCase().includes(searchTerm)
    );
    
    renderTable(filtered);
}

function updateStats(count) {
    document.getElementById('stats').textContent = `Total Records: ${count}`;
}

function validateGrades(...grades) {
    for (let grade of grades) {
        if (isNaN(grade) || grade < 0 || grade > 100) {
            alert('❌ All grades (except attendance) must be between 0 and 100!');
            return false;
        }
    }
    return true;
}

function validateAttendance(value) {
    if (isNaN(value)) {
        alert('❌ Attendance must be a numeric value!');
        return false;
    }
    return true; // no upper/lower limits
}

// Log when script is loaded
console.log('Student Record System - Lore Ann Custodio (23-1068-533)');
console.log('Script loaded and ready');