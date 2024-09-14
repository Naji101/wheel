// Select the canvas and set up the context
const wheelCanvas = document.getElementById("wheelCanvas");
const ctx = wheelCanvas.getContext("2d");
const wheelCenterX = wheelCanvas.width / 2;
const wheelCenterY = wheelCanvas.height / 2;
const wheelRadius = 250; // Increased radius to fit within the new canvas size
const loadedImages = []; // Array to store loaded images

const numberOfPrizes = prizes.length;
const prizeAngle = (2 * Math.PI) / numberOfPrizes;

console.log("numberOfPrizes", numberOfPrizes);

// Function to preload images and draw the wheel
function preloadImages() {
    let imagesLoaded = 0;
    prizes.forEach((prize, index) => {
        const img = new Image();
        img.src = prize.imageUrl;
        console.log("image", prize.name);

        img.onload = () => {
            loadedImages[index] = img;
            imagesLoaded++;
            if (imagesLoaded === numberOfPrizes) {
                drawWheel(0); // Draw the initial wheel after all images are loaded
            }
        };
        img.onerror = () => {
            console.error('Error loading image:', prize.imageUrl);
            imagesLoaded++;
            if (imagesLoaded === numberOfPrizes) {
                drawWheel(0); // Draw the initial wheel even if some images failed to load
            }
        };
    });
}

// Function to draw the wheel
function drawWheel(rotation) {
    ctx.clearRect(0, 0, wheelCanvas.width, wheelCanvas.height);
    ctx.save();
    ctx.translate(wheelCenterX, wheelCenterY);
    ctx.rotate(rotation);

    for (let i = 0; i < numberOfPrizes; i++) {
        // Draw the segments
        ctx.beginPath();
        ctx.moveTo(0, 0);
        ctx.arc(0, 0, wheelRadius, i * prizeAngle, (i + 1) * prizeAngle);
        ctx.fillStyle = i % 2 === 0 ? "#FFD700" : "#C0C0C0";
        ctx.fill();
        ctx.stroke();

        // Draw the prize text and image
        ctx.save();
        ctx.rotate((i * prizeAngle) + (prizeAngle / 2)); // Rotate to center text
        ctx.translate(wheelRadius * 0.6, 0); // Adjust the text position
        ctx.rotate(Math.PI / 2); // Rotate text to be readable

        ctx.fillStyle = "#000";
        ctx.font = "16px Arial";
        ctx.textAlign = "center";
        ctx.fillText(prizes[i].name, 0, 0); // Position the text correctly within the segment

        if (loadedImages[i]) {
            ctx.drawImage(loadedImages[i], -30, 30, 60, 60); // Image size 60x60 pixels
        }

        ctx.restore();
    }

    ctx.restore();

    // Draw the red arrow
    drawArrow();
}

// Function to draw the red arrow
function drawArrow() {
    ctx.save();
    ctx.translate(wheelCenterX + wheelRadius + 20, wheelCenterY); // Adjusted position to right of the wheel

    ctx.beginPath();
    ctx.moveTo(0, 0); // Arrow tip
    ctx.lineTo(-30, -15); // Top left edge of the arrow
    ctx.lineTo(-30, 15); // Bottom left edge of the arrow
    ctx.closePath();

    ctx.fillStyle = "red";
    ctx.fill();
    ctx.restore();
}

// Function to spin the wheel
function spinWheel() {
    const randomDegree = Math.random() * 360 + 1080; // Rotate at least 3 full turns
    let currentRotation = 0;

    const spinAnimation = setInterval(() => {
        currentRotation += 10; // Increase rotation by 10 degrees per frame
        drawWheel((currentRotation * Math.PI) / 180); // Convert degrees to radians
        if (currentRotation >= randomDegree) {
            clearInterval(spinAnimation);
            determineWinningPrize(currentRotation);
        }
    }, 16); // 60 FPS
}

// Adjusted function to determine the winning prize
function determineWinningPrize(rotation) {
    const winningSegmentIndex = Math.floor((rotation % 360) / (360 / numberOfPrizes));
    const winningPrize = prizes[numberOfPrizes - 1 - winningSegmentIndex];
    document.getElementById("prize-name").innerText = winningPrize.name;
    document.getElementById("winning-prize").style.display = "block";

    // Send data to backend to save in Firestore
    saveWinnerData(user.name, user.phone, user.location, winningPrize.name);
}

// Function to send winner data to the backend
function saveWinnerData(name, phone, location, prize) {
    fetch('/saveWinner', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: name,
            phone: phone,
            location: location,
            prize: prize
        })
    })
    .then(response => response.json())
    .then(data => {
        console.log('Winner saved successfully:', data);
    })
    .catch((error) => {
        console.error('Error saving winner:', error);
    });
}

// Initialize and preload images when the page loads
window.onload = preloadImages;
