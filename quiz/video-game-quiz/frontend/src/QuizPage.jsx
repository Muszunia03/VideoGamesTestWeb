import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './QuizPage.css';

const Carousel = ({ children }) => (
    <div className="carousel-container">
        <div className="carousel-content">
            {children}
        </div>
    </div>
);

const VideoPlayer = ({ videoSrc, title }) => {
    
    const getEmbedUrl = (url) => {
        try {
            const urlObj = new URL(url);
            const videoId = urlObj.searchParams.get('v');
            if (videoId) {
                return `https://www.youtube.com/embed/${videoId}`;
            }
        } catch (e) {
            if (url.includes("youtube.com/embed/")) {
                return url;
            }
        }
        return url; 
    };

    const embedUrl = getEmbedUrl(videoSrc);

    return (
        <div className="video-player-container">
            <h2 className="video-title">{title}</h2>
            <div className="video-placeholder">
                <iframe
                    width="100%"
                    height="400px"
                    src={embedUrl}
                    title={title}
                    frameBorder="0"
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                    allowFullScreen
                ></iframe>
            </div>
        </div>
    );
};

const FeaturedQuizCard = ({ title, description, imageSrc, onClick }) => (
    <div className="featured-quiz-card" onClick={onClick}>
        <img src={imageSrc} alt={title} className="featured-quiz-image"/>
        <div className="featured-quiz-info">
            <h3>🔥 {title}</h3>
            <p>{description}</p>
            <button className="start-button">Start Challenge</button>
        </div>
    </div>
);

function QuizPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const isActive = (path) => location.pathname === path;

      //    <div className="quiz-card" onClick={() => navigate('/quiz/retro')}></div>
    const quizData = [
        { path: '/quiz/retro', title: 'Retro Classics', description: 'Explore the golden age of gaming with pixelated challenges!', imgSrc: '/RetroLogo.png' },
        { path: '/quiz/latest', title: 'Latest Releases', description: 'Stay current! Quizzes on the hottest games hitting the market now.', imgSrc: '/NewRealeseLogo.png' },
        { path: '/quiz/images', title: 'Guess the Game from Image', description: 'Can you identify the game just from a screenshot or character?', imgSrc: '/GuessGameLogo.png' },
        { path: '/quiz/genre', title: 'Genre Challenge', description: 'From RPGs to FPS, master quizzes across diverse game genres.', imgSrc: '/GenreLogo.png' },
        { path: '/quiz/platformmatch', title: 'Platform Match', description: 'Match the platforms for the given game', imgSrc: '/PlatformLogo.png' },
        { path: '/quiz/rating', title: 'Rating Estimator', description: 'Guess if the game had rating above or below a given value', imgSrc: '/RatingLogo.png' },
        { path: '/quiz/multifact', title: 'Multi-Fact Mix', description: 'Guess the game without any restrictions', imgSrc: '/GuessEvery.png' },
    ];

    const today = new Date();
    const dayOfYear = Math.floor((today - new Date(today.getFullYear(), 0, 0)) / 1000 / 60 / 60 / 24); 
    const featuredQuizIndex = dayOfYear % quizData.length;

    const featuredQuiz = quizData[featuredQuizIndex];


    return (
        <div className="page-wrapper quiz-page-with-navbar-spacing">
            <div className="quiz-page">

                <h1>Test Your Video Game Knowledge!</h1>
                <p>Dive into different eras and genres, and see how well you know the gaming world. Select a quiz type to begin your challenge!</p>

                <VideoPlayer 
                    videoSrc='https://www.youtube.com/watch?v=5mGuCdlCcNM' 
                    title="Welcome to the Gaming Quiz Hub!"
                />
                
                <div className="featured-quiz-section">
                    <h2>🏆 Featured Quiz of the Day!</h2>
                    <FeaturedQuizCard
                        title={featuredQuiz.title}
                        description={featuredQuiz.description}
                        imageSrc={featuredQuiz.imgSrc}
                        onClick={() => navigate(featuredQuiz.path)}
                    />
                </div>

                <div className="carousel-section">
                    <h2>🕹️ Explore All Quizzes </h2>
                    <Carousel>
                        {quizData.map((quiz, index) => (
                            <div 
                                key={index}
                                className="quiz-card carousel-item" 
                                onClick={() => navigate(quiz.path)}
                            >
                                <img src={quiz.imgSrc} alt={quiz.title} />
                                <h3>{quiz.title}</h3>
                                <p className="quiz-description">{quiz.description}</p>
                            </div>
                        ))}
                    </Carousel>
                </div>

            </div>
        </div>
    );
}

export default QuizPage;