//
//  Networking.swift
//  AirLot
//
//  Created by Przemysław Kalawski on 15/09/2019.
//  Copyright © 2019 Przemysław Kalawski. All rights reserved.
//

import Foundation

protocol Networking {
    func send(url: String, _ eventHandler: @escaping (Welcome?) -> (Void))
}

struct NetworkingImpl: Networking {
    func send(url: String, _ eventHandler: @escaping (Welcome?) -> (Void)) {
        let sessionConfig = URLSessionConfiguration.default
        sessionConfig.timeoutIntervalForRequest = 60.0
        sessionConfig.timeoutIntervalForResource = 60.0
        let session = URLSession(configuration: sessionConfig)
        let task = session.dataTask(with: URL(string: url)!) { (data, response, error) in
            do {
                let welcome = try JSONDecoder().decode(Welcome.self, from: data!)
                eventHandler(welcome)
            } catch {
                eventHandler(nil)
            }
        }
        task.resume()
    }
}
