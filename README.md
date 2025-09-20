# Test Case Generator

[![CI](https://github.com/Caelan0501/devops_testgen/actions/workflows/ci.yml/badge.svg)](https://github.com/Caelan0501/devops_testgen/actions/workflows/ci.yml)

A simple tool to generate test cases for software development, built with a DevOps mindset.

---

## Features
- Generate structured or random test cases
- Export to JSON/CSV for integration with other tools
- Ready for CI/CD pipelines with automated tests and linting
- Containerized for consistent development and deployment

---

## Getting Started

### Prerequisites
- Python 3.11+
- `pip` package manager
- (Optional) Docker

### Installation
Clone the repository and install dependencies:
````Bash
git clone https://github.com/<your-username>/testcase-generator.git
cd testcase-generator
pip install -r requirements.txt
````

### Usage
Run the generator from the command line:
````bash
python src/main.py
````

### Development Workflow
This project follows a DevOps-inspired workflow:

1. Branching: Create feature branches for new work.

2. Testing: Add unit tests under tests/ and run with pytest.

3. CI/CD: Every push triggers GitHub Actions:
   * Linting with flake8
   * Automated tests with pytest
   * Deployment (future step)

### Project Structure
```
testcase-generator/
├── .github/workflows/ci.yml   # GitHub Actions pipeline
├── src/                       # Source code
│   └── main.py
├── tests/                     # Test files
│   └── test_main.py
├── docs/                      # Documentation and diagrams
│   └── architecture.md
├── README.md
├── LICENSE
├── requirements.txt
└── CHANGELOG.md
```
### Roadmap
- [ ] Implement basic test case generator
- [ ] Add CLI options for input/output formats
- [ ] Dockerize application 
- [ ] Deploy minimal API version (Flask/FastAPI)
- [ ] Add monitoring/logging

### License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.