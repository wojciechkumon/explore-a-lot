//
//  ResultsViewController.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 15/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import UIKit

class ResultsViewController: UIViewController {
    
    private var results: [WelcomeElement] = []
    private let networking: Networking = NetworkingImpl()
    @IBOutlet weak var resultsTableView: UITableView!
    @IBOutlet weak var indicator: UIActivityIndicatorView!
    @IBOutlet weak var noResultsLabel: UILabel!
    @IBOutlet weak var topLabel: UILabel!
    @IBOutlet weak var tryAgainButton: CustomButton!
    @IBOutlet weak var goBackButton: CustomButton!
    
    @IBOutlet weak var waitingStackView: UIStackView!
    private var selectedIndex: Int!
    var url: String!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.resultsTableView.dataSource = self
        self.resultsTableView.delegate = self
        
        self.resultsTableView.rowHeight = 221
        sendRequest()
    }

    @IBAction func tryAgain(_ sender: CustomButton) {
        sendRequest()
    }
    
    @IBAction func goBack(_ sender: CustomButton) {
        guard let mainVC = UIStoryboard.init(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "MainViewController") as? MainViewController else { return }
        self.present(mainVC, animated: true, completion: nil)
    }
    
    private func sendRequest() {
        
        UIView.animate(withDuration: 1.0) {
            self.waitingStackView.isHidden = false
            self.resultsTableView.isHidden = true
            self.noResultsLabel.isHidden = true
            self.tryAgainButton.isHidden = true
            self.goBackButton.isHidden = true
            self.topLabel.isHidden = true
        }
        
        networking.send(url: url) { (welcome) -> (Void) in
            if let welcome = welcome {
                self.results = welcome
            }
            DispatchQueue.main.async {
                self.resultsTableView.reloadData()
                UIView.animate(withDuration: 1.0, animations: {
                    self.waitingStackView.isHidden = true
                    if !self.results.isEmpty {
                        self.resultsTableView.isHidden = false
                        self.topLabel.isHidden = false
                    } else {
                        self.noResultsLabel.isHidden = false
                        self.tryAgainButton.isHidden = false
                        self.goBackButton.isHidden = false
                    }
                })
            }
        }
    }
}

extension ResultsViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return results.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let resultCell = tableView.dequeueReusableCell(withIdentifier: ResultCell.className, for: indexPath) as? ResultCell else {
            return UITableViewCell()
        }
        tableView.beginUpdates()
        resultCell.model = results[indexPath.row]
        tableView.endUpdates()
        return resultCell
    }
}

extension ResultsViewController: UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        guard let finalVC = UIStoryboard.init(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "FinalViewController") as? FinalViewController else { return }
        finalVC.item = results[indexPath.row]
        self.present(finalVC, animated: true, completion: nil)
    }
}
