// Prelim Grade Calculator - JavaScript Console Version
// This version can be run in Node.js or browser console

function calculatePrelimGrade() {
    console.log("=".repeat(60));
    console.log("         📊 PRELIM GRADE CALCULATOR");
    console.log("=".repeat(60));
    console.log();

    // In Node.js, you would use readline or prompts library
    // For browser console, use prompt()
    
    // Get inputs
    const attendance = parseInt(prompt("Enter number of attendances (0-5):"));
    const lab1 = parseFloat(prompt("Enter Lab Work 1 grade (0-100):"));
    const lab2 = parseFloat(prompt("Enter Lab Work 2 grade (0-100):"));
    const lab3 = parseFloat(prompt("Enter Lab Work 3 grade (0-100):"));

    // Validate inputs
    if (isNaN(attendance) || isNaN(lab1) || isNaN(lab2) || isNaN(lab3)) {
        console.error("❌ Error: Please enter valid numbers for all fields!");
        return;
    }

    if (attendance < 0 || attendance > 5) {
        console.error("❌ Error: Attendance must be between 0 and 5!");
        return;
    }

    if (lab1 < 0 || lab1 > 100 || lab2 < 0 || lab2 > 100 || lab3 < 0 || lab3 > 100) {
        console.error("❌ Error: Lab grades must be between 0 and 100!");
        return;
    }

    console.log("\n" + "=".repeat(60));
    console.log("                    INPUT VALUES");
    console.log("=".repeat(60));
    console.log(`Attendance Score:              ${attendance}`);
    console.log(`Lab Work 1 Grade:              ${lab1.toFixed(2)}`);
    console.log(`Lab Work 2 Grade:              ${lab2.toFixed(2)}`);
    console.log(`Lab Work 3 Grade:              ${lab3.toFixed(2)}`);

    // Calculate attendance score (each attendance = 20%)
    const attendanceScore = attendance * 20.0;

    // Calculate absences
    const absences = 5 - attendance;

    // Check if failed due to absences (4 or more absences)
    if (absences >= 4) {
        console.log("\n" + "=".repeat(60));
        console.log("                  COMPUTED VALUES");
        console.log("=".repeat(60));
        console.log(`Attendance Score:              ${attendanceScore.toFixed(2)}`);
        console.log(`Lab Work 1 Grade:              ${lab1.toFixed(2)}`);
        console.log(`Lab Work 2 Grade:              ${lab2.toFixed(2)}`);
        console.log(`Lab Work 3 Grade:              ${lab3.toFixed(2)}`);
        console.log(`Lab Work Average:              0.00`);
        console.log(`Class Standing:                0.00`);

        console.log("\n" + "=".repeat(60));
        console.log("            REQUIRED PRELIM EXAM SCORES");
        console.log("=".repeat(60));
        console.log(`To Pass (75):                  N/A`);
        console.log(`For Excellent (100):           N/A`);

        console.log("\n" + "=".repeat(60));
        console.log("                     REMARKS");
        console.log("=".repeat(60));
        console.log(`❌ FAILED.`);
        console.log();
        console.log(`You have ${absences} absence(s). Having 4 or more absences`);
        console.log(`results in automatic failure.`);
        console.log();
        console.log(`⚠️ Passing the Prelim period is IMPOSSIBLE due to excessive absences.`);
        console.log("=".repeat(60));
        return;
    }

    // Calculate Lab Work Average
    const labAvg = (lab1 + lab2 + lab3) / 3.0;

    // Calculate Class Standing (40% Attendance + 60% Lab Work Average)
    const classStanding = (attendanceScore * 0.40) + (labAvg * 0.60);

    // Calculate Required Prelim Exam Scores
    // Formula: Prelim Grade = (Prelim Exam × 0.30) + (Class Standing × 0.70)
    const requiredToPass = (75.0 - (classStanding * 0.70)) / 0.30;
    const requiredForExcellent = (100.0 - (classStanding * 0.70)) / 0.30;

    // Display computed values
    console.log("\n" + "=".repeat(60));
    console.log("                  COMPUTED VALUES");
    console.log("=".repeat(60));
    console.log(`Attendance Score:              ${attendanceScore.toFixed(2)}`);
    console.log(`Lab Work 1 Grade:              ${lab1.toFixed(2)}`);
    console.log(`Lab Work 2 Grade:              ${lab2.toFixed(2)}`);
    console.log(`Lab Work 3 Grade:              ${lab3.toFixed(2)}`);
    console.log(`Lab Work Average:              ${labAvg.toFixed(2)}`);
    console.log(`Class Standing:                ${classStanding.toFixed(2)}`);

    console.log("\n" + "=".repeat(60));
    console.log("            REQUIRED PRELIM EXAM SCORES");
    console.log("=".repeat(60));
    console.log(`To Pass (75):                  ${requiredToPass.toFixed(2)}`);
    console.log(`For Excellent (100):           ${requiredForExcellent.toFixed(2)}`);

    // Generate remarks
    console.log("\n" + "=".repeat(60));
    console.log("                     REMARKS");
    console.log("=".repeat(60));

    if (requiredToPass > 100) {
        console.log(`❌ FAILED.`);
        console.log();
        console.log(`Passing the Prelim period is IMPOSSIBLE because the required`);
        console.log(`score (${requiredToPass.toFixed(2)}) exceeds 100.`);
        console.log();
        console.log(`⚠️ Achieving an Excellent grade (100) is NOT possible.`);
        console.log(`The required score (${requiredForExcellent.toFixed(2)}) exceeds 100.`);
    } else if (requiredForExcellent > 100) {
        console.log(`✓ Passing is POSSIBLE!`);
        console.log();
        console.log(`You need to score ${requiredToPass.toFixed(2)} on the Prelim Exam to pass (75).`);
        console.log();
        console.log(`⚠️ Achieving an Excellent grade (100) is NOT possible.`);
        console.log(`The required score (${requiredForExcellent.toFixed(2)}) exceeds 100.`);
    } else {
        console.log(`✓ Passing is POSSIBLE!`);
        console.log();
        console.log(`You need to score ${requiredToPass.toFixed(2)} on the Prelim Exam to pass (75).`);
        console.log();
        console.log(`🌟 Achieving an Excellent grade (100) is POSSIBLE!`);
        console.log(`You need to score ${requiredForExcellent.toFixed(2)} on the Prelim Exam.`);
    }

    console.log("=".repeat(60));
}

// Run the calculator
// For browser: Open browser console and paste this code, then call calculatePrelimGrade()
// For Node.js: Uncomment the line below
// calculatePrelimGrade();

// Export for module usage
if (typeof module !== 'undefined' && module.exports) {
    module.exports = { calculatePrelimGrade };
}